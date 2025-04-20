package org.acme.model.Planes;

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
public class Planes extends Vehicles {

    private String planeType;
    private int horsepower;

}
