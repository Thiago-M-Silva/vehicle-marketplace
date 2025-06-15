package org.acme.abstracts;

import java.time.LocalDateTime;
import java.util.UUID;

import org.acme.enums.*;
import org.acme.model.Bikes.Bikes;
import org.acme.model.Boats.Boats;
import org.acme.model.Cars.Cars;
import org.acme.model.Planes.Planes;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
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
    private float price;
    private EStatus status;
    private ECategory category;
    private EColors color;
    private EFuelType fuelType;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String description;
}
