package db.migration.mongock;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;

import java.time.Instant;
import java.util.UUID;

import org.bson.Document;

@ChangeUnit(id = "V001_create_vehicleDocuments", order = "001", author = "thiago", systemVersion = "1")
public class V001_CreateVehicleDocuments {

    private final MongoDatabase mongoDatabase;

    public V001_CreateVehicleDocuments(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    @Execution
    public void migrationMethod() {
        // --- Create collection if not exists ---
        if (!mongoDatabase.listCollectionNames()
                .into(new java.util.ArrayList<>())
                .contains("vehicleDocuments")) {
            mongoDatabase.createCollection("vehicleDocuments");
        }

        // --- Add indexes ---
        mongoDatabase.getCollection("vehicleDocuments")
                .createIndex(Indexes.ascending("vehicleId"));

        mongoDatabase.getCollection("vehicleDocuments")
                .createIndex(
                        Indexes.compoundIndex(
                                Indexes.ascending("vehicleId"),
                                Indexes.ascending("fileName")
                        ),
                        new IndexOptions().unique(true)
                );

        mongoDatabase.getCollection("vehicleDocuments")
                .createIndex(Indexes.descending("uploadDate"));

        // --- Save migration history ---
        saveHistory("V001", "Create vehicleDocuments collection and indexes", "thiago");
    }

    private void saveHistory(String version, String description, String author) {
        if (!mongoDatabase.listCollectionNames()
                .into(new java.util.ArrayList<>())
                .contains("_schemaHistory")) {
            mongoDatabase.createCollection("_schemaHistory");
        }

        mongoDatabase.getCollection("_schemaHistory").insertOne(
                new Document()
                        .append("id", UUID.randomUUID().toString())
                        .append("version", version)
                        .append("description", description)
                        .append("author", author)
                        .append("executedAt", Instant.now())
                        .append("success", true)
        );
    }
}
