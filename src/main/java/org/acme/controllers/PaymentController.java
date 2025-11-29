package org.acme.controllers;

import org.acme.dtos.PaymentDTO;
import org.acme.dtos.RentDTO;
import org.acme.services.StripeService;

import com.stripe.exception.StripeException;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/payment")
@Produces("application/json")
@Consumes("application/json")
public class PaymentController {
    
    @Inject StripeService stripeService;

    /**
     * Creates a payment intent for a vehicle marketplace transaction using Stripe.
     * 
     * This endpoint processes a payment request by creating a Stripe PaymentIntent
     * with marketplace-specific details including seller account, application fees,
     * and vehicle information.
     * 
     * @param request the payment request DTO containing:
     *                - amount: the payment amount in the smallest currency unit
     *                - currency: the ISO 4217 currency code (e.g., "usd")
     *                - sellerAccountId: the Stripe account ID of the seller
     *                - applicationFee: the fee amount retained by the marketplace
     *                - vehicleId: the unique identifier of the vehicle being purchased
     *                - vehicleType: the type/category of the vehicle
     *                - receiptEmail: the email address for the payment receipt
     * 
     * @return a Response with status 200 (OK) containing the PaymentIntent JSON
     *         on success, or status 500 (INTERNAL_SERVER_ERROR) with error details
     *         if the payment creation fails
     */
    @POST
    public Response createPayment(PaymentDTO request) {
        try {
            var intent = stripeService.createMarketplacePayment(
                    request.amount(),
                    request.currency(),
                    request.sellerAccountId(),
                    request.applicationFee(),
                    request.vehicleId(),
                    request.vehicleType(),
                    request.receiptEmail()
            );
            return Response.ok(intent.toJson()).build(); 
        } catch (StripeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"" + e.getMessage() + "\"}")
                           .build();
        }
    }

    /**
     * Creates a renting payment by delegating to the Stripe integration.
     *
     * <p>Exposed as an HTTP POST at path "/renting". Expects a RentDTO containing
     * the following information: vehicleId, vehicleType, customerId,
     * sellerAccountId and applicationFee.
     *
     * <p>The controller calls stripeService.createRentalSubscription(...) to
     * create the underlying rental subscription/payment. On success the created
     * subscription is returned as JSON in the response body.
     *
     * @param request RentDTO containing required rental and payment information:
     *                vehicleId(), vehicleType(), customerId(),
     *                sellerAccountId(), applicationFee()
     * @return javax.ws.rs.core.Response with HTTP 200 (OK) and the subscription
     *         JSON on success; HTTP 500 (Internal Server Error) with a JSON
     *         error message if an exception occurs.
     */
    @POST
    @Path("/renting")
    public Response createRentingPayment(RentDTO request){
        try {
            var rent = stripeService.createRentalSubscription(
                request.vehicleId(),
                request.vehicleType(),
                request.customerId(),
                request.sellerAccountId(),
                request.applicationFee()
            );

            return Response.ok(rent.toJson()).build();
        } catch (StripeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\":\"" + e.getMessage() + "\"}")
                .build();
        }
    }
}
