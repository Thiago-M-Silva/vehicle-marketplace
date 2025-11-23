package org.acme.infra;

import java.util.UUID;

import org.acme.dtos.InvoiceFallbackDTO;
import org.acme.dtos.PaymentIntentFallbackDTO;
import org.acme.dtos.PlanFallbackDTO;
import org.acme.dtos.PriceFallbackDTO;
import org.acme.dtos.ProductFallbackDTO;
import org.acme.services.EmailService;
import org.acme.services.StripeService;
import org.acme.services.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/webhook")
public class StripeWebhookResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(StripeWebhookResource.class);

    @Inject
    StripeService stripeService;
    @Inject
    EmailService emailService;
    @Inject
    UtilsService utilsService;
    @Inject
    ObjectMapper objectMapper; 

    @POST
    @Consumes("application/json")
    public Response handleWebhook(String payload, @HeaderParam("Stripe-Signature") String sigHeader) {

        Event event = null;

        try {
            event = Webhook.constructEvent(payload, sigHeader, stripeService.getWebhookSecret());
        } catch (SignatureVerificationException e) {
            LOGGER.warn("Webhook error: Invalid signature", e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            LOGGER.warn("Webhook error: Invalid payload", e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        StripeObject stripeObject = event.getDataObjectDeserializer()
                .getObject()
                .orElse(null);

       if (stripeObject == null) {
            try {
                JsonNode root = objectMapper.readTree(payload);
                JsonNode dataObject = root.path("data").path("object");

                if (!dataObject.isMissingNode()) {

                    switch (event.getType()) {

                        // -----------------------
                        // PAYMENT INTENT
                        // -----------------------
                        case "payment_intent.created":
                        case "payment_intent.succeeded":
                        case "payment_intent.payment_failed": {
                            PaymentIntentFallbackDTO dto = objectMapper.treeToValue(dataObject, PaymentIntentFallbackDTO.class);
                            stripeObject = StripeFallbackFactory.fromDTO(dto);
                            break;
                        }

                        // -----------------------
                        // INVOICE
                        // -----------------------
                        case "invoice.paid":
                        case "invoice.payment_failed": {
                            InvoiceFallbackDTO dto = objectMapper.treeToValue(dataObject, InvoiceFallbackDTO.class);
                            stripeObject = StripeFallbackFactory.fromDTO(dto);
                            break;
                        }

                        // -----------------------
                        // PRODUCT
                        // -----------------------
                        case "product.created": {
                            ProductFallbackDTO dto = objectMapper.treeToValue(dataObject, ProductFallbackDTO.class);
                            stripeObject = StripeFallbackFactory.fromDTO(dto);
                            break;
                        }

                        // -----------------------
                        // PRICE
                        // -----------------------
                        case "price.created": {
                            PriceFallbackDTO dto = objectMapper.treeToValue(dataObject, PriceFallbackDTO.class);
                            stripeObject = StripeFallbackFactory.fromDTO(dto);
                            break;
                        }

                        // -----------------------
                        // PLAN (depreciado, mas ainda enviado)
                        // -----------------------
                        case "plan.created": {
                            PlanFallbackDTO dto = objectMapper.treeToValue(dataObject, PlanFallbackDTO.class);
                            stripeObject = StripeFallbackFactory.fromDTO(dto);
                            break;
                        }
                    }
                }

                LOGGER.warn("Fallback JSON deserialization used for event type {}", event.getType());

            } catch (Exception e) {
                LOGGER.error("Fallback JSON deserialization failed", e);
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }

        if (stripeObject == null) {
            LOGGER.error("stripeObject is STILL null. Event type: {}", event.getType());
            return Response.ok().build(); 
        }

        switch (event.getType()) {

            case "payment_intent.created":
            case "payment_intent.succeeded": {
                PaymentIntent pi = (PaymentIntent) stripeObject;
                LOGGER.info("PaymentIntent processed: {}", pi.getId());

                utilsService.updateOwner(
                        pi.getMetadata().get("seller_stripe_id"),
                        pi.getReceiptEmail(),
                        UUID.fromString(pi.getMetadata().get("vehicle_id")),
                        pi.getMetadata().get("vehicle_type")
                );
                emailService.sendPaymentSuccessEmail(pi);
                break;
            }

            case "payment_intent.payment_failed": {
                PaymentIntent pi = (PaymentIntent) stripeObject;
                LOGGER.info("PaymentIntent FAILED: {}", pi.getId());
                emailService.sendPaymentFailedEmail(pi);
                break;
            }

            case "invoice.paid": {
                Invoice inv = (Invoice) stripeObject;
                LOGGER.info("Invoice PAID: {}", inv.getId());

                utilsService.updateOwner(
                        inv.getMetadata().get("seller_stripe_id"),
                        inv.getCustomerEmail(),
                        UUID.fromString(inv.getMetadata().get("vehicle_id")),
                        inv.getMetadata().get("vehicle_type")
                );
                emailService.sendInvoicePaidEmail(inv);
                break;
            }

            case "invoice.payment_failed": {
                Invoice inv = (Invoice) stripeObject;
                LOGGER.info("Invoice FAILED: {}", inv.getId());

                utilsService.updateOwner(
                        inv.getMetadata().get("seller_stripe_id"),
                        inv.getCustomerEmail(),
                        UUID.fromString(inv.getMetadata().get("vehicle_id")),
                        inv.getMetadata().get("vehicle_type")
                );
                emailService.sendInvoiceFailedEmail(inv);
                break;
            }

            default:
                LOGGER.warn("Unhandled event type: {}", event.getType());
        }

        return Response.ok().build();
    }
}