package org.acme.services;

import java.io.InputStream;
import java.io.OutputStream;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GridFSService {
    
    @Inject
    MongoClient mongoClient;

    private GridFSBucket getBucket() {
        MongoDatabase database = mongoClient.getDatabase("vehicle-marketplace");
        return GridFSBuckets.create(database, "vehicleDocuments");
    }

    public ObjectId uploadFile(String filename, String contentType, InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("uploadFile - InputStream não pode ser nulo");            
        }
        
        GridFSUploadOptions options = new GridFSUploadOptions()
            .chunkSizeBytes(1024 * 255) // 255 KB
            .metadata(new Document("contentType", contentType)
                            .append("uploadedAt", System.currentTimeMillis()));
        return getBucket().uploadFromStream(filename, inputStream, options);        
    }

    public void downloadFile(String vehicleId, OutputStream outputStream){
        getBucket().downloadToStream(vehicleId, outputStream);
    }
    
    public void deleteFile(String fileId) {
        getBucket().delete(new org.bson.types.ObjectId(fileId));
    }
}
