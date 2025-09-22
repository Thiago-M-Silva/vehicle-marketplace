package org.acme.dtos;

import java.io.InputStream;

import org.jboss.resteasy.reactive.PartType;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;

public class VehicleDocumentRequestDTO {
    @FormParam("vehicle")
    @PartType(MediaType.APPLICATION_JSON)
    public String vehicles;
    
    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public InputStream file;

    @FormParam("filename")
    @PartType(MediaType.TEXT_PLAIN)
    public String filename;

    @FormParam("contentType")
    @PartType(MediaType.TEXT_PLAIN)
    public String contentType;
}
