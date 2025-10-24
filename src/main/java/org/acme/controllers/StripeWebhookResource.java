package org.acme.controllers;

import com.google.gson.JsonSyntaxException;
import com.stripe.exception.StripeException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;

import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.acme.services.StripeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/webhook")
public class StripeWebhookResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(StripeWebhookResource.class);

    @Inject
    StripeService stripeService;

    @POST
    @Consumes("application/json")
    public Response handleWebhook(String payload, @HeaderParam("Stripe-Signature") String sigHeader) {
        Event event;
        try {
            event = Webhook.constructEvent(
                payload, sigHeader, stripeService.getWebhookSecret()
            );
        } catch (JsonSyntaxException e) {
            LOGGER.warn("Webhook error: Invalid payload", e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (SignatureVerificationException e) {
            LOGGER.warn("Webhook error: Invalid signature", e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        // Deserialize the nested object inside the event
        StripeObject stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);

        // Handle the event
        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                LOGGER.info("Payment for {} succeeded.", paymentIntent.getAmount());
                // TODO: Fulfill the single trade purchase (e.g., update vehicle status to 'SOLD')
                break;
            case "invoice.paid":
                Invoice invoice = (Invoice) stripeObject;
                LOGGER.info("Invoice {} paid for subscription {}.", invoice.getId(), invoice.getSubscription());
                // TODO: Fulfill the rental for the billing period (e.g., mark vehicle as 'RENTED' until next billing date)
                break;
            case "payment_intent.payment_failed":
                PaymentIntent paymentIntentFailed = (PaymentIntent) stripeObject;
                LOGGER.warn("Payment for {} failed.", paymentIntentFailed.getAmount());
                break;
            // ... handle other event types like payment_intent.payment_failed
            default:
                LOGGER.warn("Unhandled event type: {}", event.getType());
        }
        return Response.ok().build();
    }
}
