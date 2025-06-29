package org.acme.model;

import org.acme.abstracts.Vehicles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class representing a bike. This class extends the Vehicles class and adds
 * specific attributes for bikes.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bikes")
public class Bikes extends Vehicles {

    private int horsepower;
    private String bikeType;

}
