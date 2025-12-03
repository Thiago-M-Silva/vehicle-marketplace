package org.acme.controllers;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;

import org.acme.model.VehicleDocuments;
import org.acme.repositories.VehicleDocumentsRepository;
import org.acme.services.GridFSService;
import org.acme.services.VehicleService;
import org.jboss.resteasy.reactive.MultipartForm;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("vehicles/{vehicleId}/documents")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.MULTIPART_FORM_DATA)
public class VehicleDocumentController {
    
    @Inject
    VehicleService vehicleService;

    @Inject
    VehicleDocumentsRepository repository;

    @Inject
    GridFSService gridFSService;

    public static class DocumentUploadForm {
        @FormParam("file")
        public InputStream file;

        @FormParam("filename")
        public String filename;

        @FormParam("contentType")
        public String contentType;
    }

    /**
     * Uploads a document for a specific vehicle.
     *
     * This method handles an HTTP POST multipart/form-data request to upload a document
     * and associate it with the vehicle identified by {@code vehicleId}. The upload
     * payload is provided via a {@code DocumentUploadForm} which should contain the
     * original filename, the content type (MIME type), and the file binary/content.
     *
     * On success, the persisted VehicleDocuments representation is returned in the
     * response body with HTTP 200 (OK). If an error occurs while saving the document,
     * the method responds with HTTP 500 (Internal Server Error) and an error message.
     *
     * @param vehicleId the UUID of the vehicle to which the document will be attached (extracted from the request path)
     * @param form the multipart form carrying upload data (filename, contentType, file)
     * @return a JAX-RS {@code Response} containing the created {@code VehicleDocuments} on success,
     *         or an error message with HTTP 500 on failure
     */
    @POST
    @Path("/upload")
    public Response uploadDocument(@PathParam("vehicleId") UUID vehicleId, @MultipartForm DocumentUploadForm form){
        try {
            VehicleDocuments doc = vehicleService.saveDocument(
                vehicleId,
                form.filename,
                form.contentType,
                form.file
            );
            
            return Response.ok(doc).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                         .entity("Erro ao fazer upload: " + e.getMessage())
                         .build();
        }
    }

    /**
     * Downloads a vehicle document file.
     * 
     * This endpoint retrieves a document associated with a specific vehicle and returns it
     * as a downloadable file. The document is identified by both the vehicle ID and filename.
     * 
     * @param vehicleId the unique identifier of the vehicle whose document is being downloaded
     * @param filename the name of the file to be downloaded
     * @return a Response containing the file content with appropriate headers for file download,
     *         or an error response if the document is not found or an error occurs
     * @return 200 OK with the file content if the document is found and downloaded successfully
     * @return 404 NOT_FOUND if no document exists for the given vehicle ID and filename
     * @return 500 INTERNAL_SERVER_ERROR if an exception occurs during file download
     */
    @GET
    @Path("/{filename}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadDocument(
            @PathParam("vehicleId") UUID vehicleId,
            @PathParam("filename") String filename) {
        try {
            VehicleDocuments doc = repository.find("vehicleId = ?1 and fileName = ?2", 
                                                 vehicleId, filename).firstResult();
            
            if (doc == null) return Response.status(Response.Status.NOT_FOUND).entity("Não há arquivios").build();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            gridFSService.downloadFile(filename, outputStream);

            return Response.ok(outputStream.toByteArray())
                         .header("Content-Disposition", 
                                "attachment; filename=\"" + filename + "\"")
                         .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                         .entity("Erro ao baixar arquivo: " + e.getMessage())
                         .build();
        }
    }
}
