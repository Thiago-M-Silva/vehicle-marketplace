package org.acme.services;

import java.io.InputStream;
import java.io.OutputStream;

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

    public void uploadFile(String filename, String contentType, InputStream inputStream) {
        GridFSUploadOptions options = new GridFSUploadOptions()
            .chunkSizeBytes(1024 * 255) // 255 KB
            .metadata(new org.bson.Document("contentType", contentType));
        getBucket().uploadFromStream(filename, inputStream, options);        
    }
    public void downloadFile(String filename, OutputStream outputStream){
        getBucket().downloadToStream(filename, outputStream);
    }
    
    public void deleteFile(String fileId) {
        getBucket().delete(new org.bson.types.ObjectId(fileId));
    }
}
