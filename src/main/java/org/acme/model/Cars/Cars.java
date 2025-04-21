package org.acme.model.Cars;

import org.acme.abstracts.Vehicles;

import jakarta.persistence.Entity;

@Entity
public class Cars extends Vehicles {

    private String carType;
    private int horsepower;
    private String transmissionType;
    private int numberOfTires;
}
