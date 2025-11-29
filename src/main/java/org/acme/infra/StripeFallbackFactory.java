package org.acme.infra;

import org.acme.dtos.InvoiceFallbackDTO;
import org.acme.dtos.PaymentIntentFallbackDTO;
import org.acme.dtos.PlanFallbackDTO;
import org.acme.dtos.PriceFallbackDTO;
import org.acme.dtos.ProductFallbackDTO;

import com.stripe.model.Invoice;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Plan;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.StripeObject;

/**
 * Factory class for converting Stripe fallback DTOs to their corresponding Stripe object representations.
 * 
 * This factory provides static methods to transform data transfer objects (DTOs) into Stripe domain objects
 * when the primary Stripe API is unavailable or as a fallback mechanism. Each conversion method maps the
 * relevant fields from the DTO to the target Stripe object.
 * 
 * Supported conversions:
 * - {@link PaymentIntentFallbackDTO} to {@link PaymentIntent}
 * - {@link InvoiceFallbackDTO} to {@link Invoice}
 * - {@link ProductFallbackDTO} to {@link Product}
 * - {@link PriceFallbackDTO} to {@link Price}
 * - {@link PlanFallbackDTO} to {@link Plan}
 */
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

    public static StripeObject fromDTO(ProductFallbackDTO dto) {
        Product product = new Product();
        product.setId(dto.id);
        product.setName(dto.name);
        product.setType(dto.type);
        return product;
    }

    public static StripeObject fromDTO(PriceFallbackDTO dto) {
        Price price = new Price();
        price.setId(dto.id);
        price.setCurrency(dto.currency);
        price.setUnitAmount(dto.unit_amount);
        return price;
    }

    public static StripeObject fromDTO(PlanFallbackDTO dto) {
        Plan plan = new Plan();
        plan.setId(dto.id);
        plan.setAmount(dto.amount);
        plan.setCurrency(dto.currency);
        plan.setInterval(dto.interval);
        return plan;
    }
}
