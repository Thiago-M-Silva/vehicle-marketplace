package org.acme.abstracts;

import org.acme.enums.*;

/**
 * Abstract class representing a vehicle. This class serves as a base for all
 * vehicle types.
 */
public abstract class Vehicles {

    String name;
    String brand;
    int age;
    float price;
    EStatus status; //enum
    ECategory category; //enum
    EColors color;//enum
    EFuelType fuelType; //enum
}
