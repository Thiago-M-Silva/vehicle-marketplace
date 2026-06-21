package org.acme.repositories;

import org.acme.model.VehicleDocuments;

import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VehicleDocumentsRepository implements PanacheMongoRepositoryBase<VehicleDocuments, String> {}
