
import org.acme.dtos.PaymentDTO;
import org.acme.dtos.RentDTO;
import org.acme.services.StripeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Subscription;
import jakarta.ws.rs.core.Response;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



package org.acme.controllers;



@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private StripeService stripeService;

    @InjectMocks
    private PaymentController paymentController;

    private PaymentDTO paymentDTO;
    private RentDTO rentDTO;

    @BeforeEach
    void setUp() {
        paymentDTO = new PaymentDTO(
                1000L,
                "usd",
                "acct_seller123",
                100L,
                "vehicle_001",
                "sedan",
                "buyer@example.com"
        );

        rentDTO = new RentDTO(
                "vehicle_001",
                "sedan",
                "cus_customer123",
                "acct_seller123",
                50L
        );
    }

    @Test
    void testCreatePaymentSuccess() throws StripeException {
        PaymentIntent mockIntent = new PaymentIntent();
        mockIntent.setId("pi_1234567890");
        when(stripeService.createMarketplacePayment(
                1000L, "usd", "acct_seller123", 100L, "vehicle_001", "sedan", "buyer@example.com"
        )).thenReturn(mockIntent);

        Response response = paymentController.createPayment(paymentDTO);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        verify(stripeService, times(1)).createMarketplacePayment(
                1000L, "usd", "acct_seller123", 100L, "vehicle_001", "sedan", "buyer@example.com"
        );
    }

    @Test
    void testCreatePaymentStripeException() throws StripeException {
        when(stripeService.createMarketplacePayment(
                anyLong(), anyString(), anyString(), anyLong(), anyString(), anyString(), anyString()
        )).thenThrow(new StripeException("Invalid payment intent"));

        Response response = paymentController.createPayment(paymentDTO);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Invalid payment intent"));
    }

    @Test
    void testCreateRentingPaymentSuccess() throws StripeException {
        Subscription mockSubscription = new Subscription();
        mockSubscription.setId("sub_1234567890");
        when(stripeService.createRentalSubscription(
                "vehicle_001", "sedan", "cus_customer123", "acct_seller123", 50L
        )).thenReturn(mockSubscription);

        Response response = paymentController.createRentingPayment(rentDTO);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        verify(stripeService, times(1)).createRentalSubscription(
                "vehicle_001", "sedan", "cus_customer123", "acct_seller123", 50L
        );
    }

    @Test
    void testCreateRentingPaymentStripeException() throws StripeException {
        when(stripeService.createRentalSubscription(
                anyString(), anyString(), anyString(), anyString(), anyLong()
        )).thenThrow(new StripeException("Rental subscription failed"));

        Response response = paymentController.createRentingPayment(rentDTO);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Rental subscription failed"));
    }
}
