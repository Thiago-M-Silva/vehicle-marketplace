package org.acme.dtos;

import java.io.InputStream;

import org.acme.abstracts.Vehicles;

import jakarta.ws.rs.FormParam;

public class VehicleDocumentRequestDTO {
    //FIXME here is the problem with multipart
    // @FormParam("vehicle")
    public Vehicles vehicles;
    
    @FormParam("file")
    public InputStream file;

    @FormParam("filename")
    public String filename;

    @FormParam("contentType")
    public String contentType;
}
