package org.acme.model;

import java.time.Instant;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonId;

/**
 * Class representing vehicle documents stored in MongoDB. This class includes
 * details about the vehicle ID, file name, content type, and timestamps for upload and update.
 * Used for storing documents such as registration, insurance, etc.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MongoEntity(collection = "vehicleDocuments")
public class VehicleDocuments {

    @BsonId
    public String id;

    @NotNull
    public String vehicleId;

    @NotNull
    public String fileName;

    @NotNull
    public String contentType;

    public Instant uploadDate;
    public Instant updateDate;
}
