package org.acme.model;

import org.acme.abstracts.Vehicles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "boats")
public class Boats extends Vehicles {

    private String model;
    private String engineType;
    private int horsepower;
    private String transmissionType;
    private int numberOfCabins;
    private String boatType;

}
