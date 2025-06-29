package org.acme.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "vehicleDocuments")
public class VehicleDocuments {
    public ObjectId id;
    public UUID vehicleId;
    public String fileName;
    public byte[] data;
    public String contentType;
    public LocalDateTime uploadDate = LocalDateTime.now();
    public LocalDateTime updateDate = LocalDateTime.now();
    
}
