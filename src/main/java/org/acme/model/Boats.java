package org.acme.model;

import org.acme.abstracts.Vehicles;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Boats extends Vehicles {

    private String make;
    private String model;
    private int year;
    private String color;
    private String engineType;
    private int horsepower;
    private String transmissionType;
    private int numberOfDoors;
    private boolean isElectric;

}
