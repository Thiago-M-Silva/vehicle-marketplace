package org.acme.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;

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
    
    /**
     * Creates a connected Stripe account for use in a marketplace flow.
     *
     * <p>This method creates a Stripe "custom" account in Brazil ("BR") and associates it with
     * the provided email address. It requests the "card_payments" and "transfers" capabilities so
     * the connected account can accept card payments and receive transfers.
     *
     * <p>Behavior and considerations:
     * <ul>
     *   <li>Requires Stripe to be configured (API key and client initialization) before calling.</li>
     *   <li>Creating a Custom account may trigger additional onboarding and verification steps
     *       in Stripe; the account may remain restricted until those are completed.</li>
     *   <li>Persist the returned account ID if you need to reference the connected account later.</li>
     *   <li>Validate the email parameter before calling to avoid predictable API errors.</li>
     * </ul>
     *
     * @param email the email address to associate with the new connected account
     * @return the Stripe account ID for the newly created connected account (e.g. "acct_...")
     * @throws com.stripe.exception.StripeException if the Stripe API call fails (network error,
     *         authentication error, invalid parameters, etc.)
     */
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

    /**
     * Generates a Stripe account onboarding link for the specified connected account.
     *
     * This creates an AccountLink via the Stripe API which directs the user to complete
     * the onboarding flow for a Stripe Connect account.
     *
     * @param accountId the Stripe connected account ID (e.g., "acct_...") for which to create the onboarding link
     * @return a URL string that can be provided to the user to begin the Stripe onboarding process
     * @throws StripeException if the Stripe API call fails (for example due to network issues, invalid credentials,
     *                         or invalid account ID)
     *
     * Notes:
     * - The returned URL is provided by Stripe and should be used as the redirect target for onboarding.
     * - The refresh and return URLs used when creating the AccountLink are currently placeholders and should be
     *   replaced with appropriate endpoints in your application to handle abandonment and successful completion.
     * - Account links are intended for short-term use; consult Stripe documentation for lifetime and usage constraints.
     */
    public String generateOnboardingLink(String accountId) throws StripeException {
        AccountLinkCreateParams params = AccountLinkCreateParams.builder()
                .setAccount(accountId)
                .setRefreshUrl("https://example.com/reauth") // TODO: update this where to send if user abadons
                .setReturnUrl("https://example.com/return") // TODO: update this  where to send after onboarding
                .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                .build();
        
        AccountLink link = AccountLink.create(params);
        return link.getUrl();
    }

    public PaymentIntent createMarketplacePayment(
            Long amount,
            String currency,
            String sellerAccountId, 
            Long applicationFee,
            UUID vehicleId,
            String vehicleType,
            String receiptEmail
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
                    )
                    .setReceiptEmail(receiptEmail)
                    .putAllMetadata(Map.of(
                            "vehicle_id", vehicleId.toString(),
                            "vehicle_type", vehicleType,
                            "seller_stripe_id", sellerAccountId
                    ));

            if (applicationFee != null && applicationFee > 0) {
                    builder.setApplicationFeeAmount(applicationFee);
            }

            PaymentIntentCreateParams params = builder.build();
            return PaymentIntent.create(params);
    }

    /**
     * Finds an existing Stripe Customer by the given email or creates a new one.
     *
     * <p>The method queries Stripe for customers matching the provided email. If one or more
     * customers are returned, the first customer from the result set is returned. If no
     * matching customer is found, a new Customer is created with the provided name and email
     * and the newly-created Customer is returned.
     *
     * @param email the email address to search for (and to set on a newly-created customer)
     * @param name  the display name to assign to a newly-created customer (ignored if an existing customer is returned)
     * @return the existing Customer matching the provided email, or a newly-created Customer if none was found
     * @throws com.stripe.exception.StripeException if an error occurs while calling the Stripe API (listing or creating customers)
     */
    public Customer findOrCreateCustomer(String email, String name) throws StripeException {
        var existingCustomers = Customer.list((Map<String, Object>) CustomerCreateParams.builder().setEmail(email).setBalance(1L).build());
       
        if (!((StripeCollection<Customer>) existingCustomers).getData().isEmpty()) {
            return ((StripeCollection<Customer>) existingCustomers).getData().get(0);
        }

        CustomerCreateParams customerParams = CustomerCreateParams.builder()
                .setName(name)
                .setEmail(email)
                .build();
        return Customer.create(customerParams);
    }

    //TODO: Review this method
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
