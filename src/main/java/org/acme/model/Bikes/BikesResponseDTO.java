package org.acme.model.Bikes;

public record BikesResponseDTO(
    String name, 
    String brand, 
    int year
) {
    // public BikesResponseDTO(Bikes bike) {
    //     this(
    //         bike.getName(), 
    //         bike.getBrand(), 
    //         bike.getYear()
    //     );
    // }
}
