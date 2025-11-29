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
import jakarta.inject.Inject;

@ApplicationScoped
public class StripeService {
    @ConfigProperty(name = "stripe.api.key")
    String stripeApiKey;

    @ConfigProperty(name = "stripe.webhook.secret")
    String webhookSecret;

    @Inject VehicleService vehicleService;

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

/**
 * Creates a Stripe PaymentIntent for a marketplace transaction.
 * 
 * This method initializes a payment intent that transfers funds to a seller's Stripe account
 * while applying an optional application fee for the marketplace. The payment supports
 * automatic payment methods and includes vehicle metadata for tracking.
 * 
 * @param amount the payment amount in the smallest currency unit (e.g., cents for USD)
 * @param currency the ISO 4217 currency code (e.g., "usd", "eur")
 * @param sellerAccountId the Stripe Connect account ID of the seller receiving the funds
 * @param applicationFee the marketplace fee amount in the smallest currency unit, or null if no fee applies
 * @param vehicleId the unique identifier of the vehicle being purchased
 * @param vehicleType the type or category of the vehicle
 * @param receiptEmail the email address where the payment receipt will be sent
 * @return a PaymentIntent object representing the created marketplace payment
 * @throws StripeException if the Stripe API request fails or returns an error
 */
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

/**
 * Creates a Stripe subscription for a vehicle rental.
 * 
 * This method handles the creation of a recurring subscription for renting a vehicle.
 * It ensures that the vehicle has associated Stripe product and price objects,
 * creating them if necessary. The subscription is configured with transfer data
 * to route payments to the seller's account, and an optional application fee.
 * 
 * @param vehicleId the unique identifier of the vehicle being rented
 * @param vehicleType the type/category of the vehicle (used to query the vehicle service)
 * @param customerId the Stripe customer ID of the renter
 * @param sellerAccountId the Stripe connected account ID of the vehicle seller
 * @param applicationFee the application fee amount in the smallest currency unit, or null if no fee applies
 * 
 * @return the created {@link Subscription} object with the latest invoice and payment intent expanded
 * 
 * @throws StripeException if an error occurs while communicating with the Stripe API
 */
    public Subscription createRentalSubscription(
        UUID vehicleId,
        String vehicleType,
        String customerId,
        String sellerAccountId,
        Long applicationFee
    ) throws StripeException {

        var vehicle = vehicleService.findById(vehicleType, vehicleId);

        String productId = vehicle.getStripeProductId();
        if (productId == null) {
            Product product = Product.create(
                    ProductCreateParams.builder()
                            .setName("Rental - " + vehicle.getModel())
                            .setType(ProductCreateParams.Type.SERVICE)
                            .build()
            );
            productId = product.getId();
            vehicle.setStripeProductId(productId);
        }

        String priceId = vehicle.getStripePriceId();
        if (priceId == null) {
            Price price = Price.create(
                    PriceCreateParams.builder()
                            .setProduct(productId)
                            .setUnitAmount(vehicle.getRentalPriceMonthly().longValue())
                            .setCurrency("brl")
                            .setRecurring(
                                    PriceCreateParams.Recurring.builder()
                                            .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
                                            .build()
                            )
                            .build()
            );
            priceId = price.getId();
            vehicle.setStripePriceId(priceId);
        }

        SubscriptionCreateParams.Builder sub = SubscriptionCreateParams.builder()
                .setCustomer(customerId)
                .addItem(SubscriptionCreateParams.Item.builder()
                        .setPrice(priceId)
                        .build()
                )
                .setPaymentBehavior(SubscriptionCreateParams.PaymentBehavior.DEFAULT_INCOMPLETE)
                .addExpand("latest_invoice.payment_intent")
                .setTransferData(
                        SubscriptionCreateParams.TransferData.builder()
                                .setDestination(sellerAccountId)
                                .build()
                );

        if (applicationFee != null && applicationFee > 0) {
            sub.setApplicationFeePercent(calculateApplicationFeePercent(vehicle.getRentalPriceMonthly().longValue(), applicationFee));
        }

        return Subscription.create(sub.build());
    }

/**
 * Creates a new Stripe customer with the provided email and name.
 *
 * <p>This method builds a CustomerCreateParams instance using the supplied
 * email and name, then calls the Stripe API to create and return the
 * corresponding Customer object.</p>
 *
 * @param email the customer's email address to associate with the Stripe customer; should be a valid email
 * @param name  the customer's full name or display name
 * @return the created Stripe Customer object as returned by the Stripe API
 * @throws StripeException if the request to the Stripe API fails or returns an error
 */
     public Customer createCustomer(String email, String name) throws StripeException {
       CustomerCreateParams params = CustomerCreateParams.builder()
            .setEmail(email)
            .setName(name)
            .build();

        return Customer.create(params);
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
