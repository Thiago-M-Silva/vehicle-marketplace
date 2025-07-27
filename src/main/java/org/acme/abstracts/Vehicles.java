package org.acme.abstracts;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.acme.enums.*;
import org.acme.model.Bikes;
import org.acme.model.Boats;
import org.acme.model.Cars;
import org.acme.model.Planes;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract class representing a vehicle. This class serves as a base for all
 * vehicle types.
 */
@MappedSuperclass
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type"
)
@JsonSubTypes({
  @JsonSubTypes.Type(value = Bikes.class, name = "bikes"),
  @JsonSubTypes.Type(value = Cars.class, name = "cars"),
  @JsonSubTypes.Type(value = Boats.class, name = "boats"),
  @JsonSubTypes.Type(value = Planes.class, name = "planes")
})

@Getter
@Setter
public abstract class Vehicles extends PanacheEntityBase {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String brand;
    private int year;

    @Column(columnDefinition = "money")
    private BigDecimal price;
    private int storage;

    private String model;
    private int horsepower;

    private String transmissionType;
    private ECategory category;
    private EColors color;
    private EFuelType fuelType;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createDate;

    @UpdateTimestamp
    private Instant updateDate;

    private String description;
}
