package org.acme.controllers;

import org.acme.services.StripeService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/stripe")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StripeResource {

    @Inject StripeService stripeService;

    // 1️⃣ Create connected account
    @POST
    @Path("/account")
    public Response createAccount(@QueryParam("email") String email) {
        try {
            String accountId = stripeService.createConnectedAccount(email);
            return Response.ok("{\"accountId\":\"" + accountId + "\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"" + e.getMessage() + "\"}")
                           .build();
        }
    }

    // 2️⃣ Generate onboarding link
    @GET
    @Path("/account/{id}/onboarding")
    public Response generateOnboarding(@PathParam("id") String accountId) {
        try {
            String link = stripeService.generateOnboardingLink(accountId);
            return Response.ok("{\"url\":\"" + link + "\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"" + e.getMessage() + "\"}")
                           .build();
        }
    }

    // 3️⃣ Create a marketplace payment
    public static class PaymentRequest {
        public Long amount;
        public String currency;
        public String sellerAccountId;
        public Long applicationFee;
    }

    @POST
    @Path("/payment")
    public Response createPayment(PaymentRequest request) {
        try {
            var intent = stripeService.createMarketplacePayment(
                    request.amount,
                    request.currency,
                    request.sellerAccountId,
                    request.applicationFee
            );
            return Response.ok(intent.toJson()).build(); // includes client_secret
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"" + e.getMessage() + "\"}")
                           .build();
        }
    }
}