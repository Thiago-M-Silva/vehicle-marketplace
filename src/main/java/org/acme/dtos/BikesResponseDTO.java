package org.acme.dtos;

public record BikesResponseDTO(
    String name, 
    String brand, 
    int year
) {
}
