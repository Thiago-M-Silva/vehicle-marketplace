package org.acme.services;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stripe.model.Invoice;
import com.stripe.model.PaymentIntent;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
@ApplicationScoped
public class EmailService {

    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    @Inject
    ReactiveMailer mailer;

    private void send(Uni<Void> mail) {
        mail.subscribe().with(
                success -> LOGGER.info("Email sent successfully!"),
                failure -> LOGGER.log(Level.SEVERE, "Failed to send email", failure)
        );
    }

    public void sendWelcomeEmail(String to, String name) {
        String subject = "Welcome to Vehicle Marketplace!";
        String body = String.format("<h1>Welcome %s!</h1><p>Thank you for joining our platform.</p>", name);

        send(mailer.send(Mail.withHtml(to, subject, body)));
    }

    public void sendPaymentSuccessEmail(PaymentIntent paymentIntent) {
        if (paymentIntent.getReceiptEmail() == null) {
            return;
        }

        String to = paymentIntent.getReceiptEmail();
        String subject = "Vehicle Marketplace - Your Payment was Successful!";
        BigDecimal amount = BigDecimal.valueOf(paymentIntent.getAmount()).movePointLeft(2);

        String body = String.format(
                "<h1>Payment Confirmation</h1>"
                + "<p>Dear Customer,</p>"
                + "<p>We are happy to inform you that your payment of <strong>%.2f %s</strong> was successful.</p>"
                + "<p>Thank you for your business!</p>"
                + "<hr>"
                + "<p><small>Vehicle Marketplace © 2025<br/>"
                + "Support: support@vehiclemarketplace.com</small></p>",
                amount, paymentIntent.getCurrency().toUpperCase()
        );

        send(mailer.send(Mail.withHtml(to, subject, body)));
    }

    public void sendInvoicePaidEmail(Invoice invoice) {
        if (invoice.getCustomerEmail() == null) {
            return;
        }

        String to = invoice.getCustomerEmail();
        String subject = "Your Subscription Invoice has been Paid!";
        BigDecimal amount = BigDecimal.valueOf(invoice.getAmountPaid()).movePointLeft(2);
        String body = String.format(
                "<h1>Subscription Payment Confirmation</h1>"
                + "<p>Dear Customer,</p>"
                + "<p>Your invoice for subscription %s has been paid successfully.</p>"
                + "<p>Amount: <strong>%.2f %s</strong></p>"
                + "<p>Thank you for your continued business!</p>",
                invoice.getSubscription(), amount, invoice.getCurrency().toUpperCase()
        );

        send(mailer.send(Mail.withHtml(to, subject, body)));
    }

    public void sendPaymentFailedEmail(PaymentIntent paymentIntent) {
        if (paymentIntent.getReceiptEmail() == null) {
            return;
        }

        String to = paymentIntent.getReceiptEmail(); 
        String subject = "Your Payment was failed, please try again!";

        String body = String.format(
                "<h1>Payment Failed Confirmation</h1>"
                + "<p>Dear Customer,</p>"
                + "<p>Please verify your data and try again.</p>"
                + "<p>Thank you for your business!</p>"
        );

        send(mailer.send(Mail.withHtml(to, subject, body)));
    }

    public void sendInvoiceFailedEmail(Invoice invoice) {
        if (invoice.getCustomerEmail() == null) {
            return;
        }

        String to = invoice.getCustomerEmail();
        String subject = "Your Subscription Invoice has failed!";
        String body = String.format(
                "<h1>Subscription Payment Confirmation</h1>"
                + "<p>Dear Customer,</p>"
                + "<p>Please verify your data and try again.</p>"
        );

        send(mailer.send(Mail.withHtml(to, subject, body)));
    }
}
