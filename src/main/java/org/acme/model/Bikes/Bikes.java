package org.acme.model.Bikes;

import org.acme.abstracts.Vehicles;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class representing a bike. This class extends the Vehicles class and adds
 * specific attributes for bikes.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bikes extends Vehicles {

    private int horsepower;
    private String bikeType;
    private String description;

}
