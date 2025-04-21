package org.acme.model.Bikes;

import org.acme.abstracts.Vehicles;

import jakarta.persistence.Entity;

/**
 * Class representing a bike. This class extends the Vehicles class and adds
 * specific attributes for bikes.
 */
@Entity
public class Bikes extends Vehicles {

    private int horsepower;
    private String bikeType;
    private String description;

}
