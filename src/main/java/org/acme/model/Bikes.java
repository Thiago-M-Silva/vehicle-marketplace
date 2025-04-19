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
public class Bikes extends Vehicles {

    private int horsepower;
    private String bikeType;
    private String description;

}
