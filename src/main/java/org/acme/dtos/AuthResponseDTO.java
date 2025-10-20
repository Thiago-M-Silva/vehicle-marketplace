package org.acme.dtos;

public record AuthResponseDTO(
    String accessToken,
    String refreshToken,
    String tokenType,
    Long expiresIn
) {}
