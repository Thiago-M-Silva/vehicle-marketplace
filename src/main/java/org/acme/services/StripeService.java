package org.acme.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.StripeCollection;
import com.stripe.model.Subscription;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.SubscriptionCreateParams;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class StripeService {
    @ConfigProperty(name = "stripe.api.key")
    String stripeApiKey;

    @ConfigProperty(name = "stripe.webhook.secret")
    String webhookSecret;

    @PostConstruct
    void init(){
        Stripe.apiKey = stripeApiKey;
    }
    
    public String createConnectedAccount(String email) throws StripeException {
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

    public String generateOnboardingLink(String accountId) throws StripeException {
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
        ) throws StripeException {
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

    public Customer findOrCreateCustomer(String email, String name) throws StripeException {
        // Check if customer already exists
        var existingCustomers = Customer.list((Map<String, Object>) CustomerCreateParams.builder().setEmail(email).setBalance(1L).build());
       
        if (!((StripeCollection<Customer>) existingCustomers).getData().isEmpty()) {
            return ((StripeCollection<Customer>) existingCustomers).getData().get(0);
        }

        // Create a new customer
        CustomerCreateParams customerParams = CustomerCreateParams.builder()
                .setName(name)
                .setEmail(email)
                .build();
        return Customer.create(customerParams);
    }

    public Subscription createSubscription(String customerId, String sellerAccountId, Long amount, String currency, Long applicationFee) throws StripeException {
        // 1. Create a Product for the rental
        ProductCreateParams productParams = ProductCreateParams.builder()
                .setName("Vehicle Rental")
                .setType(ProductCreateParams.Type.SERVICE)
                .build();
        Product product = Product.create(productParams);

        // 2. Create a Price for the Product
        PriceCreateParams priceParams = PriceCreateParams.builder()
                .setProduct(product.getId())
                .setUnitAmount(amount)
                .setCurrency(currency)
                .setRecurring(PriceCreateParams.Recurring.builder().setInterval(PriceCreateParams.Recurring.Interval.MONTH).build())
                .build();
        Price price = Price.create(priceParams);

        // 3. Create the Subscription
        SubscriptionCreateParams.Builder subscriptionBuilder = SubscriptionCreateParams.builder()
                .setCustomer(customerId)
                .addItem(SubscriptionCreateParams.Item.builder().setPrice(price.getId()).build())
                .setPaymentBehavior(SubscriptionCreateParams.PaymentBehavior.DEFAULT_INCOMPLETE)
                .addExpand("latest_invoice.payment_intent")
                .setTransferData(
                        SubscriptionCreateParams.TransferData.builder()
                                .setDestination(sellerAccountId)
                                .build()
                );

        if (applicationFee != null && applicationFee > 0) {
            subscriptionBuilder.setApplicationFeePercent(calculateApplicationFeePercent(amount, applicationFee));
        }

        return Subscription.create(subscriptionBuilder.build());
    }

    /**
     * Stripe subscriptions use application_fee_percent, not a fixed amount.
     * This calculates the percentage based on the total amount and desired fee.
     * @param totalAmount The total amount of the subscription price in cents.
     * @param feeAmount The desired fee for the platform in cents.
     * @return The fee as a percentage.
     */
    private BigDecimal calculateApplicationFeePercent(Long totalAmount, Long feeAmount) {
        if (totalAmount == null || totalAmount <= 0 || feeAmount == null || feeAmount <= 0) {
            return null;
        }
        return BigDecimal.valueOf(feeAmount * 100).divide(BigDecimal.valueOf(totalAmount), 2, RoundingMode.HALF_UP);
    }

    public String getWebhookSecret() {
        return webhookSecret;
    }
}
