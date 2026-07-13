package org.acme.controllers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;

import jakarta.ws.rs.core.Response;

import org.acme.model.VehicleDocuments;
import org.acme.repositories.VehicleDocumentsRepository;
import org.acme.services.GridFSService;
import org.acme.services.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

class VehicleDocumentControllerTest {

    @Mock
    VehicleService vehicleService;

    @Mock
    VehicleDocumentsRepository repository;

    @Mock
    GridFSService gridFSService;

    @InjectMocks
    VehicleDocumentController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadDocument_success_returnsOkWithDocument() throws Exception {
        String vehicleId = UUID.randomUUID().toString();
        VehicleDocuments saved = mock(VehicleDocuments.class);

        when(vehicleService.saveDocument(eq(UUID.fromString(vehicleId)), eq("test.txt"), eq("text/plain"), any(InputStream.class)))
                .thenReturn(saved);

        VehicleDocumentController.DocumentUploadForm form = new VehicleDocumentController.DocumentUploadForm();
        form.filename = "test.txt";
        form.contentType = "text/plain";
        form.file = new ByteArrayInputStream("hello".getBytes());

        Response resp = controller.uploadDocument(vehicleId, form);

        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertSame(saved, resp.getEntity());
    }

    @Test
    void uploadDocument_failure_returnsInternalServerError() throws Exception {
        String vehicleId = UUID.randomUUID().toString();

        when(vehicleService.saveDocument(eq(UUID.fromString(vehicleId)), anyString(), anyString(), any(InputStream.class)))
                .thenThrow(new RuntimeException("save failed"));

        VehicleDocumentController.DocumentUploadForm form = new VehicleDocumentController.DocumentUploadForm();
        form.filename = "fail.txt";
        form.contentType = "text/plain";
        form.file = new ByteArrayInputStream("data".getBytes());

        Response resp = controller.uploadDocument(vehicleId, form);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), resp.getStatus());
        assertTrue(resp.getEntity().toString().contains("Erro ao fazer upload"));
        assertTrue(resp.getEntity().toString().contains("save failed"));
    }

    @Test
    void downloadDocument_success_returnsFileBytesAndContentDisposition() throws Exception {
        String vehicleId = UUID.randomUUID().toString();
        String filename = "doc.pdf";
        VehicleDocuments doc = mock(VehicleDocuments.class);

        @SuppressWarnings("unchecked")
        PanacheQuery<VehicleDocuments> pq = mock(PanacheQuery.class);
        when(repository.find("vehicleId = ?1 and fileName = ?2", vehicleId, filename)).thenReturn(pq);
        when(pq.firstResult()).thenReturn(doc);

        byte[] fileBytes = "file-bytes".getBytes();
        doAnswer(inv -> {
            ByteArrayOutputStream out = (ByteArrayOutputStream) inv.getArgument(1);
            out.write(fileBytes);
            return null;
        }).when(gridFSService).downloadFile(eq(filename), any(ByteArrayOutputStream.class));

        Response resp = controller.downloadDocument(vehicleId, filename);

        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertTrue(resp.getHeaders().getFirst("Content-Disposition").toString().contains("filename=\"" + filename + "\""));
        Object entity = resp.getEntity();
        assertNotNull(entity);
        assertArrayEquals(fileBytes, (byte[]) entity);
    }

    @Test
    void downloadDocument_notFound_returns404() {
        String vehicleId = UUID.randomUUID().toString();
        String filename = "missing.pdf";

        @SuppressWarnings("unchecked")
        PanacheQuery<VehicleDocuments> pq = mock(PanacheQuery.class);
        when(repository.find("vehicleId = ?1 and fileName = ?2", vehicleId, filename)).thenReturn(pq);
        when(pq.firstResult()).thenReturn(null);

        Response resp = controller.downloadDocument(vehicleId, filename);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
        assertTrue(resp.getEntity().toString().contains("Não há arquivos"));
    }

    @Test
    void downloadDocument_gridFsThrows_returnsInternalServerError() throws Exception {
        String vehicleId = UUID.randomUUID().toString();
        String filename = "error.pdf";
        VehicleDocuments doc = mock(VehicleDocuments.class);

        @SuppressWarnings("unchecked")
        PanacheQuery<VehicleDocuments> pq = mock(PanacheQuery.class);
        when(repository.find("vehicleId = ?1 and fileName = ?2", vehicleId, filename)).thenReturn(pq);
        when(pq.firstResult()).thenReturn(doc);

        doThrow(new RuntimeException("io error")).when(gridFSService).downloadFile(eq(filename), any(ByteArrayOutputStream.class));

        Response resp = controller.downloadDocument(vehicleId, filename);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), resp.getStatus());
        assertTrue(resp.getEntity().toString().contains("Erro ao baixar arquivo"));
        assertTrue(resp.getEntity().toString().contains("io error"));
    }
}
