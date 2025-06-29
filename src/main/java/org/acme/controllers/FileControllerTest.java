package org.acme.controllers;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.acme.services.GridFSService;
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

@Path("/files")
@Consumes(MediaType.MULTIPART_FORM_DATA)
public class FileControllerTest {
//TODO: this class is provisory, search a way to unify this one with the vehicle controller

    @Inject
    GridFSService gridFSService;

    public static class UploadForm{
        @FormParam("file") public InputStream file;
        @FormParam("filename") public String filename;
        @FormParam("contentType") public String contentType;
    }
   
    @POST
    @Path("/upload")
    public Response upload(@MultipartForm UploadForm form){
        gridFSService.uploadFile(form.filename, form.contentType, form.file);
        return Response.ok("File uploaded successfully").build();
    }

    @GET
    @Path("/download/{filename}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("filename") String filename){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gridFSService.downloadFile(filename, outputStream);
        return Response.ok(outputStream.toByteArray())
            .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
            .build();
    }

}
