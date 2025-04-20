package org.acme.abstracts;

import java.time.LocalDateTime;

import org.acme.enums.*;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * Abstract class representing a vehicle. This class serves as a base for all
 * vehicle types.
 */
@MappedSuperclass
public abstract class Vehicles extends PanacheEntity {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String brand;
    private int year;
    private float price;
    private EStatus status;
    private ECategory category;
    private EColors color;
    private EFuelType fuelType;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String description;
}
