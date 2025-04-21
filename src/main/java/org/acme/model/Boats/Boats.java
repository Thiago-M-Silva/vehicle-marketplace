package org.acme.model.Boats;

import org.acme.abstracts.Vehicles;

import jakarta.persistence.Entity;

@Entity
public class Boats extends Vehicles {

    private String model;
    private String engineType;
    private int horsepower;
    private String transmissionType;
    private int numberOfCabins;
    private String boatType;

}
