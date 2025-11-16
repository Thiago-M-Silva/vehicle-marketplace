package org.acme.dtos;

import java.util.Map;

public record InvoiceFallbackDTO(
    String id,
    String customer_email,
    Map<String, String> metadata
) {

}
