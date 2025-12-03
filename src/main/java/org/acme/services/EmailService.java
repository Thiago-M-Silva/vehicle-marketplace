package org.acme.services;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import com.stripe.model.Invoice;
import com.stripe.model.PaymentIntent;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EmailService {

    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    private final Resend resend;

    @Inject
    public EmailService(@ConfigProperty(name = "resend.api.key") String resendApiKey) {
        this.resend = new Resend(resendApiKey);
    }

    /**
     * Sends a payment success confirmation email to the customer.
     *
     * <p>
     * This method validates that the payment intent contains the required
     * information (amount, currency, and receipt email address) before sending
     * the email. If any required field is missing, the method returns without
     * sending an email.
     *
     * <p>
     * The email body includes:
     * <ul>
     * <li>A payment confirmation header</li>
     * <li>The payment amount and currency formatted to 2 decimal places</li>
     * <li>A thank you message</li>
     * <li>Footer with company information and support contact</li>
     * </ul>
     *
     * @param paymentIntent the payment intent object containing payment details
     * including amount (in cents), currency code, and receipt email address
     * @throws ResendException if an error occurs while sending the email via
     * the Resend service (exceptions are caught and logged at INFO level)
     *
     * @see PaymentIntent
     * @see CreateEmailOptions
     * @see CreateEmailResponse
     */
    public void sendPaymentSuccessEmail(PaymentIntent paymentIntent) {
        if (paymentIntent.getAmount() == null || paymentIntent.getCurrency() == null || paymentIntent.getReceiptEmail() == null) {
            return;
        }

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

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Acme <onboarding@resend.dev>")
                .to(paymentIntent.getReceiptEmail())
                .subject("it works!")
                .html(body)
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            LOGGER.log(Level.INFO, "Email sent successfully! ID: {0}", data.getId());
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a payment confirmation email to the customer for the provided
     * invoice.
     *
     * <p>
     * Behavior: - If {@code invoice.getCustomerEmail()} is {@code null}, the
     * method returns immediately and no email is sent. - Constructs an HTML
     * message with the fixed subject "Your Subscription Invoice has been Paid!"
     * and a body containing a confirmation header, a greeting, the subscription
     * identifier, and the paid amount formatted to two decimal places followed
     * by the invoice currency in upper case. - The paid amount is derived from
     * {@code invoice.getAmountPaid()} by interpreting it as minor currency
     * units and converting it to a {@link java.math.BigDecimal} with two
     * decimal places. - Uses {@code CreateEmailOptions} with sender "Acme
     * <onboarding@resend.dev>", recipient from the invoice, the subject, and
     * the generated HTML body, then sends the email via
     * {@code resend.emails().send(...)}. - On successful send logs an INFO
     * message containing the returned email ID. - On failure catches
     * {@code ResendException} and prints the stack trace.
     *
     * <p>
     * Side effects: - Performs network I/O to deliver the email. - Writes logs
     * via {@code LOGGER} and prints stack traces on error.
     *
     * @param invoice the invoice whose customer should receive the
     * confirmation; if {@code null}, calling this method will result in a
     * {@link NullPointerException} when accessing its properties, so a non-null
     * {@code Invoice} is expected.
     */
    public void sendInvoicePaidEmail(Invoice invoice) {
        if (invoice.getCustomerEmail() == null) {
            return;
        }
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

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Acme <onboarding@resend.dev>")
                .to(invoice.getCustomerEmail())
                .subject(subject)
                .html(body)
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            LOGGER.log(Level.INFO, "Email sent successfully! ID: {0}", data.getId());
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a payment failure notification email to the customer.
     *
     * This method constructs and sends an HTML-formatted email to the receipt
     * email address associated with the payment intent, informing the customer
     * that their payment transaction has failed and prompting them to retry.
     *
     * If no receipt email is found in the payment intent, the method returns
     * early without sending an email.
     *
     * @param paymentIntent the PaymentIntent object containing payment details
     * and the recipient's email address
     *
     * @throws ResendException if an error occurs while sending the email via
     * the Resend service; exceptions are caught and logged
     *
     * @see PaymentIntent
     * @see CreateEmailOptions
     * @see CreateEmailResponse
     */
    public void sendPaymentFailedEmail(PaymentIntent paymentIntent) {
        if (paymentIntent.getReceiptEmail() == null) {
            return;
        }
        String subject = "Your Payment was failed, please try again!";
        String body = String.format(
                "<h1>Payment Failed Confirmation</h1>"
                + "<p>Dear Customer,</p>"
                + "<p>Please verify your data and try again.</p>"
                + "<p>Thank you for your business!</p>"
        );

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Acme <onboarding@resend.dev>")
                .to(paymentIntent.getReceiptEmail())
                .subject(subject)
                .html(body)
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            LOGGER.log(Level.INFO, "Email sent successfully! ID: {0}", data.getId());
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an email notification to the customer when their subscription
     * invoice payment fails.
     *
     * <p>
     * This method constructs and sends a failure notification email containing
     * instructions for the customer to verify their data and retry the payment.
     * The email is sent using the Resend email service.
     *
     * @param invoice the {@link Invoice} object containing customer email and
     * payment details. If the invoice's customer email is null, the method
     * returns early without sending an email.
     *
     * @throws ResendException if an error occurs while sending the email via
     * the Resend service. The exception is caught and logged at ERROR level.
     *
     * @see Invoice
     * @see CreateEmailOptions
     * @see CreateEmailResponse
     */
    public void sendInvoiceFailedEmail(Invoice invoice) {
        if (invoice.getCustomerEmail() == null) {
            return;
        }
        String subject = "Your Subscription Invoice has failed!";
        String body = String.format(
                "<h1>Subscription Payment Confirmation</h1>"
                + "<p>Dear Customer,</p>"
                + "<p>Please verify your data and try again.</p>"
        );

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Acme <onboarding@resend.dev>")
                .to(invoice.getCustomerEmail())
                .subject(subject)
                .html(body)
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            LOGGER.log(Level.INFO, "Email sent successfully! ID: {0}", data.getId());
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }
}
