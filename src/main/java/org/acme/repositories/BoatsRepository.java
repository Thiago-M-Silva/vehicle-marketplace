package org.acme.repositories;

import java.util.UUID;

import org.acme.model.Boats;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BoatsRepository implements PanacheRepositoryBase<Boats, UUID> {}
