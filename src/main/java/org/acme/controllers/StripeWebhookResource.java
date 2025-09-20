package org.acme.controllers;

import com.google.gson.JsonSyntaxException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.net.Webhook;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/webhook")
public class StripeWebhookResource {
    private static final String ENDPOINT_SECRET = "";

    @POST
    @Consumes("application/json")
    public void handleWebhook(String payload, @HeaderParam("Stripe-Signature") String sigHeader) {
        try {
            Event event = Webhook.constructEvent(
                payload, sigHeader, ENDPOINT_SECRET
            );

            // Handle the event
            switch (event.getType()) {
                case "payment_intent.succeeded":
                    PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
                    System.out.println("Payment for " + paymentIntent.getAmount() + " succeeded.");
                    // Then define and call a method to handle the successful payment intent.
                    // handlePaymentIntentSucceeded(paymentIntent);
                    break;
                case "payment_method.attached":
                    PaymentMethod paymentMethod = (PaymentMethod) event.getDataObjectDeserializer().getObject().orElse(null);
                    // Then define and call a method to handle the successful attachment of a PaymentMethod.
                    // handlePaymentMethodAttached(paymentMethod);
                    break;
                // ... handle other event types
                default:
                    System.out.println("Unhandled event type: " + event.getType());
            }
        } catch (JsonSyntaxException e) {
            // Invalid payload
            throw new BadRequestException();
        } catch (SignatureVerificationException e) {
            // Invalid signature
            throw new BadRequestException();
        }
    }
}
