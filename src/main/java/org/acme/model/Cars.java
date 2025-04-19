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
public class Cars extends Vehicles {

    private String carType;
    private int horsepower;
    private String transmissionType;
    private int numberOfTires;
}
