package org.acme.model;

import org.acme.abstracts.Vehicles;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "boats", schema = "mktplace")
public class Boats extends Vehicles {
    
    private int numberOfCabins;

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private Users owner;
}
