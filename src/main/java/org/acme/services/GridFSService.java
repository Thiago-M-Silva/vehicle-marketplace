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

    /**
     * Retrieves a {@link GridFSBucket} instance for the "vehicleDocuments"
     * bucket in the "vehicle-marketplace" MongoDB database.
     *
     * @return a {@link GridFSBucket} connected to the "vehicleDocuments" bucket
     */
    private GridFSBucket getBucket() {
        MongoDatabase database = mongoClient.getDatabase("vehicle-marketplace");
        return GridFSBuckets.create(database, "vehicleDocuments");
    }

    /**
     * Uploads a file to the GridFS bucket
     * <p>
     * Receives the filename, content type, and an InputStream of the file to be
     * uploaded. Configures upload options such as chunk size and metadata, then
     * uploads the file to the GridFS bucket and returns the generated ObjectId.
     * </p>
     *
     * @param filename the name of the file to be stored
     * @param contentType the MIME type of the file
     * @param inputStream the InputStream containing the file data
     * @return the {@link ObjectId} of the uploaded file in GridFS
     * @throws IllegalArgumentException if inputStream is null
     */
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

    /**
     * Downloads a file from the GridFS bucket
     * <p>
     * Receives the vehicleId (filename) and an OutputStream to write the file
     * data to. Downloads the file from the GridFS bucket and writes it to the
     * provided OutputStream.
     * </p>
     *
     * @param vehicleId the filename of the file to be downloaded
     * @param outputStream the OutputStream to write the downloaded file data
     * @throws IllegalArgumentException if outputStream is null
     */
    public void downloadFile(String vehicleId, OutputStream outputStream) {
        getBucket().downloadToStream(vehicleId, outputStream);
    }

    /**
     * Deletes a file from the GridFS bucket
     * <p>
     * Receives the fileId (as a String) of the file to be deleted. Converts the
     * fileId to an ObjectId and deletes the corresponding file from the GridFS
     * bucket.
     * </p>
     *
     * @param fileId the String representation of the ObjectId of the file to be
     * deleted
     */
    public void deleteFile(String fileId) {
        getBucket().delete(new org.bson.types.ObjectId(fileId));
    }
}
