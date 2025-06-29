package org.acme.repositories;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import org.acme.model.VehicleDocuments;

@ApplicationScoped
public class VehicleDocumentsRepository implements PanacheMongoRepository<VehicleDocuments> {}
