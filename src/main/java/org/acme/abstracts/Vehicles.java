package org.acme.abstracts;

import java.util.Date;

import org.acme.enums.*;

/**
 * Abstract class representing a vehicle. This class serves as a base for all
 * vehicle types.
 */
public abstract class Vehicles {

    private int id;
    private String name;
    private String brand;
    private int year;
    private float price;
    private EStatus status;
    private ECategory category;
    private EColors color;
    private EFuelType fuelType;
    private Date createDate;
    private Date updateDate;
    private String description;
}
