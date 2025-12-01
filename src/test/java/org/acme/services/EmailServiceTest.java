package org.acme.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import com.resend.services.emails.EmailsApi;
import com.stripe.model.PaymentIntent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.acme.services.EmailService;
import com.stripe.model.Invoice;

public class EmailServiceTest {

    private EmailService emailService;

    @BeforeEach
    public void setup() {
        emailService = new EmailService();
    }

    @Test
    public void sendPaymentSuccessEmail_shouldNotCallResend_whenRequiredFieldsMissing() {
        Resend mockResend = mock(Resend.class);
        // inject mock resend
        emailService.resend = mockResend;

        PaymentIntent pi = mock(PaymentIntent.class);
        when(pi.getAmount()).thenReturn(null); // missing amount -> early return
        when(pi.getCurrency()).thenReturn("usd");
        when(pi.getReceiptEmail()).thenReturn("to@example.com");

        // call
        emailService.sendPaymentSuccessEmail(pi);

        // verify emails() was never called
        verify(mockResend, never()).emails();
    }

    @Test
    public void sendPaymentSuccessEmail_shouldBuildAndSendEmail_whenAllFieldsPresent() throws ResendException {
        Resend mockResend = mock(Resend.class);
        EmailsApi mockEmails = mock(EmailsApi.class);
        CreateEmailResponse mockResponse = mock(CreateEmailResponse.class);

        when(mockResend.emails()).thenReturn(mockEmails);
        when(mockEmails.send(any(CreateEmailOptions.class))).thenReturn(mockResponse);
        when(mockResponse.getId()).thenReturn("email-id-123");

        // inject mock resend
        emailService.resend = mockResend;

        PaymentIntent pi = mock(PaymentIntent.class);
        when(pi.getAmount()).thenReturn(12345L); // cents
        when(pi.getCurrency()).thenReturn("usd");
        when(pi.getReceiptEmail()).thenReturn("buyer@example.com");

        // call
        emailService.sendPaymentSuccessEmail(pi);

        // capture and assert the email options passed to send
        ArgumentCaptor<CreateEmailOptions> captor = ArgumentCaptor.forClass(CreateEmailOptions.class);
        verify(mockEmails, times(1)).send(captor.capture());
        CreateEmailOptions sent = captor.getValue();

        assertNotNull(sent);
        assertEquals("buyer@example.com", sent.getTo());
        assertTrue(sent.getHtml().contains("Payment Confirmation"));
        assertTrue(sent.getHtml().contains("123.45")); // 12345 cents -> 123.45
        assertEquals("Acme <onboarding@resend.dev>", sent.getFrom());
    }

    @Test
    public void sendInvoicePaidEmail_shouldNotCallResend_whenCustomerEmailMissing() {
        Resend mockResend = mock(Resend.class);
        emailService.resend = mockResend;

        Invoice invoice = mock(Invoice.class);
        when(invoice.getCustomerEmail()).thenReturn(null);

        emailService.sendInvoicePaidEmail(invoice);

        verify(mockResend, never()).emails();
    }

    @Test
    public void sendInvoicePaidEmail_shouldBuildAndSendEmail_whenAllFieldsPresent() throws ResendException {
        Resend mockResend = mock(Resend.class);
        EmailsApi mockEmails = mock(EmailsApi.class);
        CreateEmailResponse mockResponse = mock(CreateEmailResponse.class);

        when(mockResend.emails()).thenReturn(mockEmails);
        when(mockEmails.send(any(CreateEmailOptions.class))).thenReturn(mockResponse);
        when(mockResponse.getId()).thenReturn("email-id-456");

        emailService.resend = mockResend;

        Invoice invoice = mock(Invoice.class);
        when(invoice.getCustomerEmail()).thenReturn("subscriber@example.com");
        when(invoice.getAmountPaid()).thenReturn(50000L); // cents
        when(invoice.getCurrency()).thenReturn("usd");
        when(invoice.getSubscription()).thenReturn("sub_12345");

        emailService.sendInvoicePaidEmail(invoice);

        ArgumentCaptor<CreateEmailOptions> captor = ArgumentCaptor.forClass(CreateEmailOptions.class);
        verify(mockEmails, times(1)).send(captor.capture());
        CreateEmailOptions sent = captor.getValue();

        assertNotNull(sent);
        assertEquals("subscriber@example.com", sent.getTo());
        assertEquals("Your Subscription Invoice has been Paid!", sent.getSubject());
        assertTrue(sent.getHtml().contains("Subscription Payment Confirmation"));
        assertTrue(sent.getHtml().contains("500.00")); // 50000 cents -> 500.00
        assertTrue(sent.getHtml().contains("sub_12345"));
        assertEquals("Acme <onboarding@resend.dev>", sent.getFrom());
    }

    @Test
    public void sendPaymentFailedEmail_shouldNotCallResend_whenReceiptEmailMissing() {
        Resend mockResend = mock(Resend.class);
        emailService.resend = mockResend;

        PaymentIntent pi = mock(PaymentIntent.class);
        when(pi.getReceiptEmail()).thenReturn(null);

        emailService.sendPaymentFailedEmail(pi);

        verify(mockResend, never()).emails();
    }

    @Test
    public void sendPaymentFailedEmail_shouldBuildAndSendEmail_whenReceiptEmailPresent() throws ResendException {
        Resend mockResend = mock(Resend.class);
        EmailsApi mockEmails = mock(EmailsApi.class);
        CreateEmailResponse mockResponse = mock(CreateEmailResponse.class);

        when(mockResend.emails()).thenReturn(mockEmails);
        when(mockEmails.send(any(CreateEmailOptions.class))).thenReturn(mockResponse);
        when(mockResponse.getId()).thenReturn("email-id-789");

        emailService.resend = mockResend;

        PaymentIntent pi = mock(PaymentIntent.class);
        when(pi.getReceiptEmail()).thenReturn("customer@example.com");

        emailService.sendPaymentFailedEmail(pi);

        ArgumentCaptor<CreateEmailOptions> captor = ArgumentCaptor.forClass(CreateEmailOptions.class);
        verify(mockEmails, times(1)).send(captor.capture());
        CreateEmailOptions sent = captor.getValue();

        assertNotNull(sent);
        assertEquals("customer@example.com", sent.getTo());
        assertEquals("Your Payment was failed, please try again!", sent.getSubject());
        assertTrue(sent.getHtml().contains("Payment Failed Confirmation"));
        assertEquals("Acme <onboarding@resend.dev>", sent.getFrom());
    }


    @Test
    public void sendInvoiceFailedEmail_shouldNotCallResend_whenCustomerEmailMissing() {
        Resend mockResend = mock(Resend.class);
        emailService.resend = mockResend;

        Invoice invoice = mock(Invoice.class);
        when(invoice.getCustomerEmail()).thenReturn(null);

        emailService.sendInvoiceFailedEmail(invoice);

        verify(mockResend, never()).emails();
    }

    @Test
    public void sendInvoiceFailedEmail_shouldBuildAndSendEmail_whenCustomerEmailPresent() throws ResendException {
        Resend mockResend = mock(Resend.class);
        EmailsApi mockEmails = mock(EmailsApi.class);
        CreateEmailResponse mockResponse = mock(CreateEmailResponse.class);

        when(mockResend.emails()).thenReturn(mockEmails);
        when(mockEmails.send(any(CreateEmailOptions.class))).thenReturn(mockResponse);
        when(mockResponse.getId()).thenReturn("email-id-999");

        emailService.resend = mockResend;

        Invoice invoice = mock(Invoice.class);
        when(invoice.getCustomerEmail()).thenReturn("subscriber@example.com");

        emailService.sendInvoiceFailedEmail(invoice);

        ArgumentCaptor<CreateEmailOptions> captor = ArgumentCaptor.forClass(CreateEmailOptions.class);
        verify(mockEmails, times(1)).send(captor.capture());
        CreateEmailOptions sent = captor.getValue();

        assertNotNull(sent);
        assertEquals("subscriber@example.com", sent.getTo());
        assertEquals("Your Subscription Invoice has failed!", sent.getSubject());
        assertTrue(sent.getHtml().contains("Subscription Payment Confirmation"));
        assertEquals("Acme <onboarding@resend.dev>", sent.getFrom());
    }

}


