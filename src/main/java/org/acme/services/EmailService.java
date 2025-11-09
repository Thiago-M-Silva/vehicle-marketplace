package org.acme.services;

import java.math.BigDecimal;

import com.stripe.model.Invoice;
import com.stripe.model.PaymentIntent;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

//TODO: Improve email content, desinging a better html
@ApplicationScoped
public class EmailService {

    @Inject Mailer mailer;

    public void sendPaymentSuccessEmail(PaymentIntent paymentIntent) {
        if (paymentIntent.getReceiptEmail() == null) return; 
        
        String to = paymentIntent.getReceiptEmail(); // This might be a customer ID, you'd need to fetch the email
        String subject = "Your Payment was Successful!";
        BigDecimal amount = BigDecimal.valueOf(paymentIntent.getAmount()).movePointLeft(2);
        
        String body = String.format(
            "<h1>Payment Confirmation</h1>" +
            "<p>Dear Customer,</p>" +
            "<p>We are happy to inform you that your payment of <strong>%.2f %s</strong> was successful.</p>" +
            "<p>Thank you for your business!</p>",
            amount, paymentIntent.getCurrency().toUpperCase()
        );

        mailer.send(Mail.withHtml(to, subject, body));
    }

    public void sendInvoicePaidEmail(Invoice invoice) {
        if (invoice.getCustomerEmail() == null) return;
        
        String to = invoice.getCustomerEmail();
        String subject = "Your Subscription Invoice has been Paid!";
        BigDecimal amount = BigDecimal.valueOf(invoice.getAmountPaid()).movePointLeft(2);
        String body = String.format(
            "<h1>Subscription Payment Confirmation</h1>" +
            "<p>Dear Customer,</p>" +
            "<p>Your invoice for subscription %s has been paid successfully.</p>" +
            "<p>Amount: <strong>%.2f %s</strong></p>" +
            "<p>Thank you for your continued business!</p>",
            invoice.getSubscription(), amount, invoice.getCurrency().toUpperCase()
        );

        mailer.send(Mail.withHtml(to, subject, body));
    }

    public void sendPaymentFailedEmail(PaymentIntent paymentIntent) {
        if (paymentIntent.getReceiptEmail() == null) return; 
        
        String to = paymentIntent.getReceiptEmail(); // This might be a customer ID, you'd need to fetch the email
        String subject = "Your Payment was failed, please try again!";
        
        String body = String.format(
            "<h1>Payment Failed Confirmation</h1>" +
            "<p>Dear Customer,</p>" +
            "<p>Please verify your data and try again.</p>" +
            "<p>Thank you for your business!</p>"
        );

        mailer.send(Mail.withHtml(to, subject, body));
    }

    public void sendInvoiceFailedEmail(Invoice invoice) {
        if (invoice.getCustomerEmail() == null) return;
        
        String to = invoice.getCustomerEmail();
        String subject = "Your Subscription Invoice has failed!";
        String body = String.format(
            "<h1>Subscription Payment Confirmation</h1>" +
            "<p>Dear Customer,</p>" +
            "<p>Please verify your data and try again.</p>"
            
        );

        mailer.send(Mail.withHtml(to, subject, body));
    }
}