package org.acme.model;

import org.acme.abstracts.Vehicles;
import org.acme.enums.EStatus;

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
@Table(name = "boats", schema = "mktplace")
public class Boats extends Vehicles {
    
    private EStatus boatStatus;
    private int numberOfCabins;
    private String boatType;

}
