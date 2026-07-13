package org.acme.controllers;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.acme.model.UserDocuments;
import org.acme.repositories.UserDocumentsRepository;
import org.acme.services.GridFSService;
import org.acme.services.UserService;
import org.jboss.resteasy.reactive.MultipartForm;

import jakarta.annotation.security.PermitAll;
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

@Path("users/{userId}/documents")
@Produces(MediaType.APPLICATION_JSON)
public class UserDocumentController {

    @Inject UserService userService;
    @Inject UserDocumentsRepository repository;
    @Inject GridFSService gridFSService;

    public static class DocumentUploadForm {

        @FormParam("file")
        public InputStream file;

        @FormParam("filename")
        public String filename;

        @FormParam("contentType")
        public String contentType;
    }

    /**
     * Uploads a document for a specific user.
     *
     * This method handles an HTTP POST multipart/form-data request to upload a
     * document and associate it with the user identified by
     * {@code userId}. The upload payload is provided via a
     * {@code DocumentUploadForm} which should contain the original filename,
     * the content type (MIME type), and the file binary/content.
     *
     * On success, the persisted userDocuments representation is returned in
     * the response body with HTTP 200 (OK). If an error occurs while saving the
     * document, the method responds with HTTP 500 (Internal Server Error) and
     * an error message.
     *
     * @param userId the UUID of the user to which the document will be
     * attached (extracted from the request path)
     * @param form the multipart form carrying upload data (filename,
     * contentType, file)
     * @return a JAX-RS {@code Response} containing the created
     * {@code userDocuments} on success, or an error message with HTTP 500 on
     * failure
     */
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @PermitAll
    public Response uploadDocument(@PathParam("userId") String userId, @MultipartForm DocumentUploadForm form) {
        try {
            UserDocuments doc = userService.createUserWithDocument(
                    UUID.fromString(userId),
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
     * Downloads a user document file.
     *
     * This endpoint retrieves a document associated with a specific user and
     * returns it as a downloadable file. The document is identified by both the
     * user ID and filename.
     *
     * @param userId the unique identifier of the user whose document is
     * being downloaded
     * @param filename the name of the file to be downloaded
     * @return a Response containing the file content with appropriate headers
     * for file download, or an error response if the document is not found or
     * an error occurs
     * @return 200 OK with the file content if the document is found and
     * downloaded successfully
     * @return 404 NOT_FOUND if no document exists for the given user ID and
     * filename
     * @return 500 INTERNAL_SERVER_ERROR if an exception occurs during file
     * download
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @PermitAll
    public Response downloadDocument(@PathParam("userId") String userId) {
        try {
            UserDocuments doc = repository.find("userId = ?1", userId).firstResult();

            if (doc == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Não há arquivos").build();
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            gridFSService.downloadFile(doc.fileName, outputStream);

            return Response.ok(outputStream.toByteArray())
                    .header("Content-Disposition",
                            "attachment; filename=\"" + doc.fileName + "\"")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao baixar arquivo: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getAllUsersWithImages() {
        try {
            List<UserDocuments> docs = repository.findAll().list();
            List<String> ids = docs.stream()
                    .map(doc -> doc.userId)
                    .distinct()
                    .collect(Collectors.toList());
            return Response.ok(ids).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar lista de veículos: " + e.getMessage())
                    .build();
        }
    }
}
