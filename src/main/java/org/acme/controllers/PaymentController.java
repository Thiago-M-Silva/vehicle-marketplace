package org.acme.controllers;

import org.acme.dtos.PaymentDTO;
import org.acme.services.StripeService;

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
            return Response.ok(intent.toJson()).build(); // includes client_secret
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"" + e.getMessage() + "\"}")
                           .build();
        }
    }
}
