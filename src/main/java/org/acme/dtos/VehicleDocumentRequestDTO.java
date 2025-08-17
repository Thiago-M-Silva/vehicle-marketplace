package org.acme.dtos;

import java.io.InputStream;

import org.acme.abstracts.Vehicles;

import jakarta.ws.rs.FormParam;

public class VehicleDocumentRequestDTO {
    public Vehicles vehicles;
    
    @FormParam("file")
    public InputStream file;

    @FormParam("filename")
    public String filename;

    @FormParam("contentType")
    public String contentType;
}
