package org.acme.model.Planes;

import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlanesRepository implements PanacheRepositoryBase<Planes, UUID> {}
