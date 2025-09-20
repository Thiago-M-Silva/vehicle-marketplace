package org.acme.services;

import com.stripe.Stripe;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.PaymentIntent;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.PaymentIntentCreateParams;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StripeService {
    String stripeApiKey = System.getenv("STRIPE_API_KEY");

    @PostConstruct
    void init(){
        Stripe.apiKey = stripeApiKey;
    }
    
    public String createConnectedAccount(String email) throws Exception {
        AccountCreateParams params = AccountCreateParams.builder()
                .setType(AccountCreateParams.Type.CUSTOM)
                .setCountry("BR")
                .setEmail(email)
                .setCapabilities(
                        AccountCreateParams.Capabilities.builder()
                                .setCardPayments(
                                        AccountCreateParams.Capabilities.CardPayments.builder()
                                                .setRequested(true)
                                                .build())
                                .setTransfers(
                                        AccountCreateParams.Capabilities.Transfers.builder()
                                                .setRequested(true)
                                                .build())
                                .build())
                .build();

        Account account = Account.create(params);
        return account.getId();
    }

    public String generateOnboardingLink(String accountId) throws Exception {
        AccountLinkCreateParams params = AccountLinkCreateParams.builder()
                .setAccount(accountId)
                .setRefreshUrl("https://example.com/reauth") // where to send if user abadons
                .setReturnUrl("https://example.com/return") // where to send after onboarding
                .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                .build();
        
        AccountLink link = AccountLink.create(params);
        return link.getUrl();
    }

        public PaymentIntent createMarketplacePayment(
                Long amount,
                String currency,
                String sellerAccountId, // acct_xxx from Stripe Connect
                Long applicationFee // optional, in cents
        ) throws Exception {
                PaymentIntentCreateParams.Builder builder = PaymentIntentCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency(currency)
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .setTransferData(
                                PaymentIntentCreateParams.TransferData.builder()
                                        .setDestination(sellerAccountId)
                                        .build()
                        );

                if (applicationFee != null && applicationFee > 0) {
                        builder.setApplicationFeeAmount(applicationFee);
                }

                PaymentIntentCreateParams params = builder.build();
                return PaymentIntent.create(params);
        }
}
