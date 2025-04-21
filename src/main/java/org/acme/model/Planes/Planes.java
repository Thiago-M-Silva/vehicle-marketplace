package org.acme.model.Planes;

import org.acme.abstracts.Vehicles;

import jakarta.persistence.Entity;

@Entity
public class Planes extends Vehicles {

    private String planeType;
    private int horsepower;

}
