package org.acme.services;

import java.util.UUID;

import org.acme.model.Users;
import org.acme.repositories.UsersRepository;

import com.stripe.model.PaymentIntent;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PaymentService {
   @Inject VehicleService vehicleService;
   @Inject UsersRepository usersRepository;
   @Inject StripeService stripeService;
    
   public PaymentIntent processPayment(UUID sellerId, Long amount, String currency) throws Exception {
    Users seller = usersRepository.findById(sellerId);
    if (seller == null || seller.getStripeAccountId() == null) {
        throw new IllegalArgumentException("Seller does not have a connected account");
    }

    return stripeService.createMarketplacePayment(
            amount,
            currency,
            seller.getStripeAccountId(),
            500L // platform fee (optional), in cents
    );
}
}
