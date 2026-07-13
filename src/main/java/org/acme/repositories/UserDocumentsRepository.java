package org.acme.repositories;

import org.acme.model.UserDocuments;

import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserDocumentsRepository implements PanacheMongoRepositoryBase<UserDocuments, String> {}
