package org.acme.repositories;

import java.util.UUID;

import org.acme.model.Bikes;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BikesRepository implements PanacheRepositoryBase<Bikes, UUID> {}
