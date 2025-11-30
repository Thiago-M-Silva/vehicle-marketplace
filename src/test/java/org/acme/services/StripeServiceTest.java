package org.acme.services;

import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StripeServiceTest {

    @InjectMocks
    StripeService stripeService;

    @Mock
    VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        stripeService.stripeApiKey = "sk_test_123";
        stripeService.webhookSecret = "whsec_test";
    }

    @Test
    void testCreateConnectedAccount_success() throws StripeException {
        String email = "test@example.com";
        Account mockAccount = mock(Account.class);
        when(mockAccount.getId()).thenReturn("acct_123");
        try (MockedStatic<Account> accountMockedStatic = mockStatic(Account.class)) {
            accountMockedStatic.when(() -> Account.create(any(AccountCreateParams.class))).thenReturn(mockAccount);
            String accountId = stripeService.createConnectedAccount(email);
            assertEquals("acct_123", accountId);
        }
    }

    @Test
    void testGenerateOnboardingLink_success() throws StripeException {
        String accountId = "acct_123";
        AccountLink mockLink = mock(AccountLink.class);
        when(mockLink.getUrl()).thenReturn("https://onboarding.url");
        try (MockedStatic<AccountLink> linkMockedStatic = mockStatic(AccountLink.class)) {
            linkMockedStatic.when(() -> AccountLink.create(any(AccountLinkCreateParams.class))).thenReturn(mockLink);
            String url = stripeService.generateOnboardingLink(accountId);
            assertEquals("https://onboarding.url", url);
        }
    }

    @Test
    void testCreateMarketplacePayment_success() throws StripeException {
        PaymentIntent mockIntent = mock(PaymentIntent.class);
        try (MockedStatic<PaymentIntent> intentMockedStatic = mockStatic(PaymentIntent.class)) {
            intentMockedStatic.when(() -> PaymentIntent.create(any(PaymentIntentCreateParams.class))).thenReturn(mockIntent);
            PaymentIntent result = stripeService.createMarketplacePayment(
                    1000L, "usd", "acct_123", 100L, UUID.randomUUID(), "car", "buyer@example.com"
            );
            assertEquals(mockIntent, result);
        }
    }

    @Test
    void testFindOrCreateCustomer_existingCustomer() throws StripeException {
        Customer existingCustomer = mock(Customer.class);
        StripeCollection<Customer> collection = mock(StripeCollection.class);
        when(collection.getData()).thenReturn(List.of(existingCustomer));
        try (MockedStatic<Customer> customerMockedStatic = mockStatic(Customer.class)) {
            customerMockedStatic.when(() -> Customer.list(any(Map.class))).thenReturn(collection);
            Customer result = stripeService.findOrCreateCustomer("test@example.com", "Test User");
            assertEquals(existingCustomer, result);
        }
    }

    @Test
    void testFindOrCreateCustomer_createNewCustomer() throws StripeException {
        StripeCollection<Customer> collection = mock(StripeCollection.class);
        when(collection.getData()).thenReturn(Collections.emptyList());
        Customer newCustomer = mock(Customer.class);
        try (MockedStatic<Customer> customerMockedStatic = mockStatic(Customer.class)) {
            customerMockedStatic.when(() -> Customer.list(any(Map.class))).thenReturn(collection);
            customerMockedStatic.when(() -> Customer.create(any(CustomerCreateParams.class))).thenReturn(newCustomer);
            Customer result = stripeService.findOrCreateCustomer("test2@example.com", "Test User2");
            assertEquals(newCustomer, result);
        }
    }

    @Test
    void testCreateRentalSubscription_createsProductAndPrice() throws StripeException {
        UUID vehicleId = UUID.randomUUID();
        String vehicleType = "car";
        String customerId = "cus_123";
        String sellerAccountId = "acct_123";
        Long applicationFee = 100L;

        // Mock vehicle
        Vehicle mockVehicle = mock(Vehicle.class);
        when(mockVehicle.getStripeProductId()).thenReturn(null);
        when(mockVehicle.getModel()).thenReturn("Model X");
        when(mockVehicle.getStripePriceId()).thenReturn(null);
        when(mockVehicle.getRentalPriceMonthly()).thenReturn(BigDecimal.valueOf(2000L));
        when(vehicleService.findById(vehicleType, vehicleId)).thenReturn(mockVehicle);

        // Mock Stripe Product and Price
        Product mockProduct = mock(Product.class);
        when(mockProduct.getId()).thenReturn("prod_123");
        Price mockPrice = mock(Price.class);
        when(mockPrice.getId()).thenReturn("price_123");

        Subscription mockSubscription = mock(Subscription.class);

        try (
                MockedStatic<Product> productMockedStatic = mockStatic(Product.class); MockedStatic<Price> priceMockedStatic = mockStatic(Price.class); MockedStatic<Subscription> subscriptionMockedStatic = mockStatic(Subscription.class)) {
            productMockedStatic.when(() -> Product.create(any(ProductCreateParams.class))).thenReturn(mockProduct);
            priceMockedStatic.when(() -> Price.create(any(PriceCreateParams.class))).thenReturn(mockPrice);
            subscriptionMockedStatic.when(() -> Subscription.create(any(SubscriptionCreateParams.class))).thenReturn(mockSubscription);

            Subscription result = stripeService.createRentalSubscription(
                    vehicleId, vehicleType, customerId, sellerAccountId, applicationFee
            );
            assertEquals(mockSubscription, result);
            verify(mockVehicle).setStripeProductId("prod_123");
            verify(mockVehicle).setStripePriceId("price_123");
        }
    }

    @Test
    void testCreateCustomer_success() throws StripeException {
        Customer mockCustomer = mock(Customer.class);
        try (MockedStatic<Customer> customerMockedStatic = mockStatic(Customer.class)) {
            customerMockedStatic.when(() -> Customer.create(any(CustomerCreateParams.class))).thenReturn(mockCustomer);
            Customer result = stripeService.createCustomer("test@example.com", "Test User");
            assertEquals(mockCustomer, result);
        }
    }

    @Test
    void testCalculateApplicationFeePercent_valid() {
        BigDecimal percent = invokeCalculateApplicationFeePercent(10000L, 500L);
        assertEquals(new BigDecimal("5.00"), percent);
    }

    @Test
    void testCalculateApplicationFeePercent_invalid() {
        assertNull(invokeCalculateApplicationFeePercent(null, 100L));
        assertNull(invokeCalculateApplicationFeePercent(10000L, null));
        assertNull(invokeCalculateApplicationFeePercent(0L, 100L));
        assertNull(invokeCalculateApplicationFeePercent(10000L, 0L));
    }

    @Test
    void testGetWebhookSecret() {
        assertEquals("whsec_test", stripeService.getWebhookSecret());
    }

    // Helper to invoke private method
    private BigDecimal invokeCalculateApplicationFeePercent(Long total, Long fee) {
        try {
            var method = StripeService.class.getDeclaredMethod("calculateApplicationFeePercent", Long.class, Long.class);
            method.setAccessible(true);
            return (BigDecimal) method.invoke(stripeService, total, fee);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Minimal Vehicle stub for mocking
    public interface Vehicle {

        String getStripeProductId();

        void setStripeProductId(String id);

        String getStripePriceId();

        void setStripePriceId(String id);

        String getModel();

        BigDecimal getRentalPriceMonthly();
    }
}
