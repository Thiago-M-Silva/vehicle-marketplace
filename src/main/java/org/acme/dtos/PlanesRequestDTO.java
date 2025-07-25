package org.acme.dtos;

import java.time.LocalDateTime;

import org.acme.enums.ECategory;
import org.acme.enums.EColors;
import org.acme.enums.EFuelType;
import org.acme.enums.EStatus;

public record PlanesRequestDTO(
    String name, 
    String brand, 
    int year,
    float price,
    EStatus status,
    ECategory category,
    EColors color,
    EFuelType fuelType,
    LocalDateTime createDate,
    LocalDateTime updateDate,
    String description,
    int horsepower,
    String planeType
) {

}
