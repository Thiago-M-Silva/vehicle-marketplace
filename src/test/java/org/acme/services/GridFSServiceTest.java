package org.acme.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;

class GridFSServiceTest {

    private GridFSService gridFSService;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private GridFSBucket gridFSBucket;

    @BeforeEach
    void setUp() {
        mongoClient = mock(MongoClient.class);
        mongoDatabase = mock(MongoDatabase.class);
        gridFSBucket = mock(GridFSBucket.class);

        gridFSService = new GridFSService() {
            // @Override
            public GridFSBucket getBucket() {
                return gridFSBucket;
            }
        };

        gridFSService.mongoClient = mongoClient;

        when(mongoClient.getDatabase(anyString())).thenReturn(mongoDatabase);
    }

    @Test
    void uploadFile_shouldUploadAndReturnObjectId() {
        String filename = "test.txt";
        String contentType = "text/plain";
        byte[] data = "hello".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        ObjectId expectedId = new ObjectId();

        when(gridFSBucket.uploadFromStream(eq(filename), any(InputStream.class), any(GridFSUploadOptions.class)))
                .thenReturn(expectedId);

        ObjectId result = gridFSService.uploadFile(filename, contentType, inputStream);

        assertEquals(expectedId, result);

        ArgumentCaptor<GridFSUploadOptions> optionsCaptor = ArgumentCaptor.forClass(GridFSUploadOptions.class);
        verify(gridFSBucket).uploadFromStream(eq(filename), any(InputStream.class), optionsCaptor.capture());
        Document metadata = optionsCaptor.getValue().getMetadata();
        assertEquals(contentType, metadata.getString("contentType"));
        assertNotNull(metadata.get("uploadedAt"));
    }

    @Test
    void uploadFile_shouldThrowExceptionIfInputStreamIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
            gridFSService.uploadFile("file.txt", "text/plain", null)
        );
    }

    @Test
    void downloadFile_shouldCallDownloadToStream() {
        String vehicleId = "vehicle123";
        OutputStream outputStream = new ByteArrayOutputStream();

        gridFSService.downloadFile(vehicleId, outputStream);

        verify(gridFSBucket).downloadToStream(eq(vehicleId), eq(outputStream));
    }

    @Test
    void deleteFile_shouldCallDeleteWithObjectId() {
        ObjectId objectId = new ObjectId();
        String fileId = objectId.toHexString();

        gridFSService.deleteFile(fileId);

        ArgumentCaptor<ObjectId> captor = ArgumentCaptor.forClass(ObjectId.class);
        verify(gridFSBucket).delete(captor.capture());
        assertEquals(objectId, captor.getValue());
    }
}