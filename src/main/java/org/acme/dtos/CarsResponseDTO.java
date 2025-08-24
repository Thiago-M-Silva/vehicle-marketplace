package org.acme.dtos;

import org.acme.enums.ECategory;
import org.acme.enums.EColors;
import org.acme.enums.EFuelType;
import org.acme.enums.EStatus;

public record CarsResponseDTO(
    String name, 
    String brand, 
    int year,
    float price,
    EStatus status,
    ECategory category,
    EColors color,
    EFuelType fuelType,
    String description,
    int horsepower,
    String carType,
    String transmissionType,
    int numberOfTires
) {

}
