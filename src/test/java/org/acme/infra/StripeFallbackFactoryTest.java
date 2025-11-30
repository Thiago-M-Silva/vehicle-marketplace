package org.acme.infra;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.acme.dtos.InvoiceFallbackDTO;
import org.acme.dtos.PaymentIntentFallbackDTO;
import org.acme.dtos.PlanFallbackDTO;
import org.acme.dtos.PriceFallbackDTO;
import org.acme.dtos.ProductFallbackDTO;
import org.junit.jupiter.api.Test;
import com.stripe.model.Invoice;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Plan;
import com.stripe.model.Price;
import com.stripe.model.Product;
import java.util.HashMap;
import java.util.Map;

class StripeFallbackFactoryTest {

    @Test
    void testPaymentIntentFromDTO() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("key", "value");
        PaymentIntentFallbackDTO dto = mock(PaymentIntentFallbackDTO.class);
        when(dto.id()).thenReturn("pi_123");
        when(dto.receipt_email()).thenReturn("test@example.com");
        when(dto.metadata()).thenReturn(metadata);

        PaymentIntent result = StripeFallbackFactory.fromDTO(dto);

        assertNotNull(result);
        assertEquals("pi_123", result.getId());
        assertEquals("test@example.com", result.getReceiptEmail());
        assertEquals(metadata, result.getMetadata());
    }

    @Test
    void testInvoiceFromDTO() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("order", "12345");
        InvoiceFallbackDTO dto = mock(InvoiceFallbackDTO.class);
        when(dto.id()).thenReturn("inv_456");
        when(dto.customer_email()).thenReturn("customer@example.com");
        when(dto.metadata()).thenReturn(metadata);

        Invoice result = StripeFallbackFactory.fromDTO(dto);

        assertNotNull(result);
        assertEquals("inv_456", result.getId());
        assertEquals("customer@example.com", result.getCustomerEmail());
        assertEquals(metadata, result.getMetadata());
    }

    @Test
    void testProductFromDTO() {
        ProductFallbackDTO dto = new ProductFallbackDTO();
        dto.id = "prod_789";
        dto.name = "Test Product";
        dto.type = "service";

        Product result = (Product) StripeFallbackFactory.fromDTO(dto);

        assertNotNull(result);
        assertEquals("prod_789", result.getId());
        assertEquals("Test Product", result.getName());
        assertEquals("service", result.getType());
    }

    @Test
    void testPriceFromDTO() {
        PriceFallbackDTO dto = new PriceFallbackDTO();
        dto.id = "price_101";
        dto.currency = "usd";
        dto.unit_amount = 1999L;

        Price result = (Price) StripeFallbackFactory.fromDTO(dto);

        assertNotNull(result);
        assertEquals("price_101", result.getId());
        assertEquals("usd", result.getCurrency());
        assertEquals(1999L, result.getUnitAmount());
    }

    @Test
    void testPlanFromDTO() {
        PlanFallbackDTO dto = new PlanFallbackDTO();
        dto.id = "plan_202";
        dto.amount = 2999L;
        dto.currency = "eur";
        dto.interval = "month";

        Plan result = (Plan) StripeFallbackFactory.fromDTO(dto);

        assertNotNull(result);
        assertEquals("plan_202", result.getId());
        assertEquals(2999L, result.getAmount());
        assertEquals("eur", result.getCurrency());
        assertEquals("month", result.getInterval());
    }
}
