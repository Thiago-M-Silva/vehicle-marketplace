package org.acme.repositories;

import java.util.UUID;

import org.acme.model.Planes;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlanesRepository implements PanacheRepositoryBase<Planes, UUID> {}
