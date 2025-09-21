package org.acme.dtos;

import org.acme.enums.ECategory;
import org.acme.enums.EColors;
import org.acme.enums.EFuelType;
import org.acme.enums.EStatus;

public record PlanesRequestDTO(
    String name, 
    String brand, 
    int year,
    float price,
    String model,
    int horsepower,
    String transmissionType,
    String description,
    int storage,
    EStatus vehicleStatus,
    ECategory category,
    EColors color,
    EFuelType fuelType,
    UsersResponseDTO owner
) {

}
