package org.acme.dtos;

import java.util.UUID;

public record RentDTO(
    UUID vehicleId,
    String vehicleType,
    String customerId,
    String sellerAccountId,
    Long applicationFee
) {
}
