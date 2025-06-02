package org.acme.model.Cars;

import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CarsRepository implements PanacheRepositoryBase<Cars, UUID> {

}
