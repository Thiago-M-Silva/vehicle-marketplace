package org.acme.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum EPaymentMethods {
    CASH,
    CREDIT_CARD,
    DEBIT_CARD,
    PAYPAL,
    BANK_TRANSFER,
    CRYPTOCURRENCY,
    APPLE_PAY,
    GOOGLE_PAY,
    AMAZON_PAY,
    SAMSUNG_PAY
    // Add more colors as needed
}
