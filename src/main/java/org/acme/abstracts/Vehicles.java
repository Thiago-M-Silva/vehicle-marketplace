package org.acme.abstracts;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.acme.enums.*;
import org.acme.model.Bikes;
import org.acme.model.Boats;
import org.acme.model.Cars;
import org.acme.model.Planes;
import org.acme.model.Users;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Abstract class representing a vehicle. This class serves as a base for all
 * vehicle types.
 */
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
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Vehicles extends PanacheEntityBase {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String brand;
    private int year;
    private String model;
    private int horsepower;
    private String transmissionType;
    private String description;

    private int storage;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 50)
    public ECategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicleStatus", length = 50)
    public EStatus vehicleStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "color", length = 50)
    public EColors color;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuelType", length = 50)
    public EFuelType fuelType;

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private Users owner;

    @Column(columnDefinition = "money")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createDate;

    @UpdateTimestamp
    private Instant updateDate;

}