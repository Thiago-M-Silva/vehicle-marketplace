package org.acme.infra;

import org.acme.dtos.InvoiceFallbackDTO;
import org.acme.dtos.PaymentIntentFallbackDTO;

import com.stripe.model.Invoice;
import com.stripe.model.PaymentIntent;

public class StripeFallbackFactory {

    public static PaymentIntent fromDTO(PaymentIntentFallbackDTO dto) {
        PaymentIntent pi = new PaymentIntent();
        pi.setId(dto.id());
        pi.setReceiptEmail(dto.receipt_email());
        pi.setMetadata(dto.metadata());
        return pi;
    }

    public static Invoice fromDTO(InvoiceFallbackDTO dto) {
        Invoice inv = new Invoice();
        inv.setId(dto.id());
        inv.setCustomerEmail(dto.customer_email());
        inv.setMetadata(dto.metadata());
        return inv;
    }
}
