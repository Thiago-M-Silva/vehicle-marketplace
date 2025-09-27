package org.acme.controllers;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.acme.abstracts.Vehicles;
import org.acme.dtos.VehicleDocumentRequestDTO;
import org.acme.dtos.VehicleSearchDTO;
import org.acme.middlewares.ApiMiddleware;
import org.acme.services.GridFSService;
import org.acme.services.VehicleService;
import org.jboss.resteasy.reactive.MultipartForm;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/vehicles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleController {
    @Inject VehicleService vehicleService;
    @Inject ApiMiddleware apiMiddleware;
    @Inject GridFSService gridFSService;

    //OK
    @GET
    @Path("/get/{vehicleType}")
    public Response getAllVehicles(
        @PathParam("vehicleType") String vehicleType
    ) {
       try {
            List<Vehicles> vehicles = vehicleService.listAll(vehicleType);
            var responseDTOs = apiMiddleware.manageVehicleTypeResponseDTO(vehicleType, vehicles);
            return Response.ok(responseDTOs).build();
       } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error retrieving vehicles: " + e.getMessage())
                           .build();
       }
    }

    @GET
    @Path("/get/download/{vehicleId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("vehicleId") String vehicleId){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gridFSService.downloadFile(vehicleId, outputStream);
        
        return Response.ok(outputStream.toByteArray())
            .header("Content-Disposition", "attachment; filename=\"" + vehicleId + "\"")
            .build();
    }

    //OK
    @GET
    @Path("/get/{vehicleType}/{id}")
    public Response getVehiclesById(
        @PathParam("vehicleType") String vehicleType, 
        @PathParam("id") UUID id
    ) {
        try {
            Vehicles vehicle = vehicleService.findById(vehicleType, id);
            return Response.ok(vehicle).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Vehicle not found: " + e.getMessage())
                           .build();
        }
    }

    //OK
    @GET
    @Path("/get/search/{vehicleType}")
    public Response search(
        @PathParam("vehicleType") String vehicleType,
        @BeanParam VehicleSearchDTO searchParams
    ) {
        try {
            var vehicles = vehicleService.searchVehicle(vehicleType, searchParams);
            return Response.ok(vehicles).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Searching error: " + e.getMessage())
                        .build();
        }
    }

    //OK
    @POST
    @Path("/save/saveAllVehicles/{vehicleType}")
    public Response saveAllVehicles(
        @PathParam("vehicleType") String vehicleType, 
        List<Map<String, Object>> vehicles
    ) {
        try {
            var vehiclesRequestDTO = apiMiddleware.manageListVehiclesTypeRequestDTO(vehicleType, vehicles);
            int savedVehicles = vehicleService.saveMultipleVehicles(vehicleType, vehiclesRequestDTO);
            return Response.status(Response.Status.CREATED).entity(savedVehicles).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Error saving vehicle: " + e.getMessage())
                           .build();
        }
    }

    //OK
    @POST
    @Path("/save/{vehicleType}")
    public Response addVehicle(
        @PathParam("vehicleType") String vehicleType,
        JsonObject body 
    ) {
        try {
            Vehicles vehicle = apiMiddleware.manageVehiclesTypeRequestDTO(vehicleType, body);

            Vehicles savedVehicle = vehicleService.save(vehicleType, vehicle);
            return Response.status(Response.Status.CREATED).entity(savedVehicle).build();
        } catch (Exception e) {
            e.printStackTrace(); 
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Error saving vehicle: " + e.getMessage())
                           .build();
        }
    }

    //OK
    @POST
    @Path("/save/{vehicleType}/docs")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response saveVehicleWithDocs(
        @PathParam("vehicleType") String vehicleType,
        @MultipartForm VehicleDocumentRequestDTO data
    ){
        try {
            if (data == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Form data is required")
                    .build();
            }

            if (data.vehicles == null || data.vehicles.isBlank()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Field 'vehicles' (JSON) is required in the multipart form")
                    .build();
            }

            // Parse the JSON sent in the 'vehicles' form part (not the vehicleType path param)
            JsonObject json = Json.createReader(new StringReader(data.vehicles)).readObject();

            var vehicleRequestDTO = apiMiddleware.manageVehiclesTypeRequestDTO(vehicleType, json);

            // validate file info
            if (data.file == null || data.filename == null || data.filename.isBlank()) {
                // allow saving vehicle without file, or require file — choose behaviour
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("File and filename are required")
                    .build();
            }

            Vehicles savedVehicles = vehicleService.saveVehicleWithDocuments(
                vehicleType,
                (Vehicles) vehicleRequestDTO,
                data.file,
                data.filename,
                data.contentType
            );

            return Response.status(Response.Status.CREATED).entity(savedVehicles).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Erro ao salvar veículo e documentos: " + e.getMessage())
                .build();
        }
    }

    //OK
    @DELETE
    @Path("/delete/{vehicleType}/{id}")
    public Response deleteVehicle(
        @PathParam("vehicleType") String vehicleType, 
        @PathParam("id") UUID id
    ) {
        try {
            vehicleService.deleteById(vehicleType, id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Error deleting vehicle: " + e.getMessage())
                           .build();
        }
    }
    
    //OK
    @DELETE
    @Path("/delete/{vehicleType}")
    public Response deleteManyVehicles(
        @PathParam("vehicleType") String vehicleType, 
        List<UUID> id
    ) {
        try {
            vehicleService.deleteManyVehicles(vehicleType, id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Error deleting vehicle: " + e.getMessage())
                           .build();
        }
    }

    //OK
    @PUT
    @Path("/edit/{vehicleType}/{id}")
    public Response editVehicle(
        @PathParam("vehicleType") String vehicleType,
        @PathParam("id") UUID id,
        JsonObject body
    ){
        try {
            Vehicles vehicle = apiMiddleware.manageVehiclesTypeRequestDTO(vehicleType, body);

            vehicleService.editVehicleInfo(vehicleType, id, vehicle);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Error editing vehicle: " + e.getMessage())
                           .build();
        }
    }

}
