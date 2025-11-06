package org.acme.dtos;

import java.util.UUID;

public record PaymentDTO(
    Long amount,
    String currency,
    String sellerAccountId,
    Long applicationFee,
    UUID vehicleId,
    String vehicleType
) {
}
