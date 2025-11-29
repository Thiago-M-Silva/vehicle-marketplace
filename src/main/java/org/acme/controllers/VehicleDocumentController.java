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


    @GET
    @Path("/{filename}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadDocument(
            @PathParam("vehicleId") UUID vehicleId,
            @PathParam("filename") String filename) {
        try {
            VehicleDocuments doc = repository.find("vehicleId = ?1 and fileName = ?2", 
                                                 vehicleId, filename).firstResult();
            
            if (doc == null) return Response.status(Response.Status.NOT_FOUND).build();

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
