package org.acme.repositories;

import java.util.UUID;

import org.acme.model.Cars;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CarsRepository implements PanacheRepositoryBase<Cars, UUID> {

}
