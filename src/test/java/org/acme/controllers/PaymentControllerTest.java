package org.acme.controllers;

import org.acme.dtos.PaymentDTO;
import org.acme.dtos.RentDTO;
import org.acme.services.StripeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.stripe.exception.StripeException;
import com.stripe.exception.CardException;
import java.util.UUID;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Subscription;
import jakarta.ws.rs.core.Response;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @Mock
    private StripeService stripeService;

    @InjectMocks
    private PaymentController paymentController;

    private PaymentDTO paymentDTO;
    private RentDTO rentDTO;
    private UUID vehicleId;

    @BeforeEach
    void setUp() {
        vehicleId = UUID.randomUUID();
        paymentDTO = new PaymentDTO(
                1000L,
                "usd",
                "acct_seller123",
                100L,
                vehicleId,
                "sedan",
                "buyer@example.com"
        );

        rentDTO = new RentDTO(
                vehicleId,
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
                paymentDTO.amount(), paymentDTO.currency(), paymentDTO.sellerAccountId(),
                paymentDTO.applicationFee(), paymentDTO.vehicleId(), paymentDTO.vehicleType(), paymentDTO.receiptEmail()
        )).thenReturn(mockIntent);

        Response response = paymentController.createPayment(paymentDTO);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        verify(stripeService).createMarketplacePayment(
                paymentDTO.amount(), paymentDTO.currency(), paymentDTO.sellerAccountId(),
                paymentDTO.applicationFee(), paymentDTO.vehicleId(), paymentDTO.vehicleType(), paymentDTO.receiptEmail());
    }

    @Test
    void testCreatePaymentStripeException() throws StripeException {
        when(stripeService.createMarketplacePayment(
                anyLong(), anyString(), anyString(), anyLong(), any(UUID.class), anyString(), anyString()
        )).thenThrow(new CardException("Invalid payment intent", "card_error", "charge_123", "param", "code", "decline_code", null, null));

        Response response = paymentController.createPayment(paymentDTO);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Invalid payment intent"));
    }

    @Test
    void testCreateRentingPaymentSuccess() throws StripeException {
        Subscription mockSubscription = new Subscription();
        mockSubscription.setId("sub_1234567890");
        when(stripeService.createRentalSubscription(
                rentDTO.vehicleId(), rentDTO.vehicleType(), rentDTO.customerId(),
                rentDTO.sellerAccountId(), rentDTO.applicationFee()
        )).thenReturn(mockSubscription);

        Response response = paymentController.createRentingPayment(rentDTO);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        verify(stripeService).createRentalSubscription(rentDTO.vehicleId(), rentDTO.vehicleType(), rentDTO.customerId(),
                rentDTO.sellerAccountId(), rentDTO.applicationFee());
    }

    @Test
    void testCreateRentingPaymentStripeException() throws StripeException {
        when(stripeService.createRentalSubscription(
                any(UUID.class), anyString(), anyString(), anyString(), anyLong()
        )).thenThrow(new CardException("Invalid payment intent", "card_error", "charge_123", "param", "code", "decline_code", null, null));

        Response response = paymentController.createRentingPayment(rentDTO);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Rental subscription failed"));
    }
}
