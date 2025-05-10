package org.acme.model.Bikes;

import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BikesRepository implements PanacheRepositoryBase<Bikes, UUID> {

}
