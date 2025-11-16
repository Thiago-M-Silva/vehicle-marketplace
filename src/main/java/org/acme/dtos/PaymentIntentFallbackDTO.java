package org.acme.dtos;

import java.util.Map;

public record PaymentIntentFallbackDTO(
    String id,
    Map<String, String> metadata,
    String receipt_email
) {

}
