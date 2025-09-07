package org.acme.model;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
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
    public UUID vehicleId;
    @NotNull
    public String fileName;
    @NotNull
    public String contentType;

    @CreationTimestamp
    @Column(updatable = false)
    public Instant uploadDate;
    
    @UpdateTimestamp
    public Instant updateDate;
    
}
