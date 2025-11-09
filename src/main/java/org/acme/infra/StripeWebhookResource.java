package org.acme.infra;

import java.util.UUID;

import org.acme.services.EmailService;
import org.acme.services.StripeService;
import org.acme.services.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonSyntaxException;
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

//TODO: Improve events maneagement flow
@Path("/webhook")
public class StripeWebhookResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(StripeWebhookResource.class);

    @Inject StripeService stripeService;
    @Inject EmailService emailService;
    @Inject UtilsService utilsService;

    @POST
    @Consumes("application/json")
    public Response handleWebhook(String payload, @HeaderParam("Stripe-Signature") String sigHeader) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, stripeService.getWebhookSecret());
        } catch (JsonSyntaxException e) {
            LOGGER.warn("Webhook error: Invalid payload", e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (SignatureVerificationException e) {
            LOGGER.warn("Webhook error: Invalid signature", e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        StripeObject stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);

        switch (event.getType()) {
            case "payment_intent.succeeded" -> {
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;

                utilsService.updateOwner(
                    paymentIntent.getMetadata().get("seller_stripe_id"),
                    paymentIntent.getReceiptEmail(),
                    UUID.fromString(paymentIntent.getMetadata().get("vehicle_id")),
                    paymentIntent.getMetadata().get("vehicle_type")
                );
                
                emailService.sendPaymentSuccessEmail(paymentIntent);
            }
            case "invoice.paid" -> {
                Invoice invoice = (Invoice) stripeObject;
                //change vehicle's owner 
                //TODO: Make a time limit logic
                utilsService.updateOwner(
                    invoice.getMetadata().get("seller_stripe_id"),
                    invoice.getCustomerEmail(),
                    UUID.fromString(invoice.getMetadata().get("vehicle_id")),
                    invoice.getMetadata().get("vehicle_type")
                );
                emailService.sendInvoicePaidEmail(invoice);
            }
            case "payment_intent.payment_failed" -> {
                PaymentIntent paymentIntentFailed = (PaymentIntent) stripeObject;
                
                emailService.sendPaymentFailedEmail(paymentIntentFailed);
            }
            case "invoice.payment_failed" -> {
                Invoice invoice = (Invoice) stripeObject;
                
                //change vehicle's owner 
                //TODO: Make a time limit logic
                utilsService.updateOwner(
                    invoice.getMetadata().get("seller_stripe_id"),
                    invoice.getCustomerEmail(),
                    UUID.fromString(invoice.getMetadata().get("vehicle_id")),
                    invoice.getMetadata().get("vehicle_type")
                );

                emailService.sendInvoiceFailedEmail(invoice);
            }
            default -> {
                LOGGER.warn("Unhandled event type: {}", event.getType()); 
            }
        }
        return Response.ok().build();
    }
}