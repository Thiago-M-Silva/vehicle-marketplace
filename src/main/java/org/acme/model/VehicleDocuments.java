package org.acme.model;

import java.time.Instant;
import java.util.UUID;

import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonRepresentation;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MongoEntity(collection = "vehicleDocuments")
public class VehicleDocuments {

    @Id
    public String id;

    @NotNull
    @BsonRepresentation(BsonType.STRING)
    public UUID vehicleId;

    @NotNull
    public String fileName;

    @NotNull
    public String contentType;

    public Instant uploadDate;
    public Instant updateDate;

    @PrePersist
    public void prePersist() {
        this.uploadDate = Instant.now();
        this.updateDate = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updateDate = Instant.now();
    }
}