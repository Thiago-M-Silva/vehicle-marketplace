package org.acme.infra;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.acme.services.EmailService;
import org.acme.services.StripeService;
import org.acme.services.UtilsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import jakarta.ws.rs.core.Response;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class StripeWebhookResourceTest {

    @Mock
    private StripeService stripeService;

    @Mock
    private EmailService emailService;

    @Mock
    private UtilsService utilsService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private StripeWebhookResource resource;

    private String webhookSecret = "test_secret";
    private String validSignature = "t=1614000000,v1=test_signature";

    @BeforeEach
    void setUp() {
        when(stripeService.getWebhookSecret()).thenReturn(webhookSecret);
    }

    @Test
    void testHandleWebhookWithInvalidSignature() {
        String payload = "{}";

        mockStatic(Webhook.class);
        when(Webhook.constructEvent(payload, validSignature, webhookSecret))
                .thenThrow(new SignatureVerificationException("Invalid signature", null, null));

        Response response = resource.handleWebhook(payload, validSignature);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    void testHandleWebhookWithInvalidPayload() {
        String payload = "invalid json";

        mockStatic(Webhook.class);
        when(Webhook.constructEvent(payload, validSignature, webhookSecret))
                .thenThrow(new RuntimeException("Invalid payload"));

        Response response = resource.handleWebhook(payload, validSignature);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    void testHandlePaymentIntentSucceeded() {
        String payload = "{\"data\":{\"object\":{}}}";
        Event event = new Event();
        event.setType("payment_intent.succeeded");

        PaymentIntent paymentIntent = new PaymentIntent();
        paymentIntent.setId("pi_test");
        paymentIntent.setReceiptEmail("test@example.com");

        Map<String, String> metadata = new HashMap<>();
        metadata.put("seller_stripe_id", "seller_123");
        metadata.put("vehicle_id", UUID.randomUUID().toString());
        metadata.put("vehicle_type", "CAR");
        paymentIntent.setMetadata(metadata);

        mockStatic(Webhook.class);
        when(Webhook.constructEvent(payload, validSignature, webhookSecret))
                .thenReturn(event);

        Response response = resource.handleWebhook(payload, validSignature);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(emailService, times(1)).sendPaymentSuccessEmailTeste(paymentIntent);
        verify(utilsService, times(1)).updateOwner(anyString(), anyString(), any(UUID.class), anyString());
    }

    @Test
    void testHandlePaymentIntentFailed() {
        String payload = "{\"data\":{\"object\":{}}}";
        Event event = new Event();
        event.setType("payment_intent.payment_failed");

        PaymentIntent paymentIntent = new PaymentIntent();
        paymentIntent.setId("pi_test_fail");

        mockStatic(Webhook.class);
        when(Webhook.constructEvent(payload, validSignature, webhookSecret))
                .thenReturn(event);

        Response response = resource.handleWebhook(payload, validSignature);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(emailService, times(1)).sendPaymentFailedEmail(paymentIntent);
    }

    @Test
    void testHandleInvoicePaid() {
        String payload = "{\"data\":{\"object\":{}}}";
        Event event = new Event();
        event.setType("invoice.paid");

        Invoice invoice = new Invoice();
        invoice.setId("inv_test");
        invoice.setCustomerEmail("customer@example.com");

        Map<String, String> metadata = new HashMap<>();
        metadata.put("seller_stripe_id", "seller_123");
        metadata.put("vehicle_id", UUID.randomUUID().toString());
        metadata.put("vehicle_type", "CAR");
        invoice.setMetadata(metadata);

        mockStatic(Webhook.class);
        when(Webhook.constructEvent(payload, validSignature, webhookSecret))
                .thenReturn(event);

        Response response = resource.handleWebhook(payload, validSignature);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(emailService, times(1)).sendInvoicePaidEmail(invoice);
        verify(utilsService, times(1)).updateOwner(anyString(), anyString(), any(UUID.class), anyString());
    }

    @Test
    void testHandleInvoiceFailed() {
        String payload = "{\"data\":{\"object\":{}}}";
        Event event = new Event();
        event.setType("invoice.payment_failed");

        Invoice invoice = new Invoice();
        invoice.setId("inv_test_fail");
        invoice.setCustomerEmail("customer@example.com");

        mockStatic(Webhook.class);
        when(Webhook.constructEvent(payload, validSignature, webhookSecret))
                .thenReturn(event);

        Response response = resource.handleWebhook(payload, validSignature);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(emailService, times(1)).sendInvoiceFailedEmail(invoice);
    }

    @Test
    void testHandleUnknownEventType() {
        String payload = "{\"data\":{\"object\":{}}}";
        Event event = new Event();
        event.setType("unknown.event");

        mockStatic(Webhook.class);
        when(Webhook.constructEvent(payload, validSignature, webhookSecret))
                .thenReturn(event);

        Response response = resource.handleWebhook(payload, validSignature);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(emailService, never()).sendPaymentSuccessEmailTeste(any());
    }
}
