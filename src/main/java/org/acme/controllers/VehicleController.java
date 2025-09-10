package org.acme.controllers;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

import org.acme.abstracts.Vehicles;
import org.acme.dtos.VehicleDocumentRequestDTO;
import org.acme.dtos.VehicleSearchDTO;
import org.acme.middlewares.ApiMiddleware;
import org.acme.services.GridFSService;
import org.acme.services.VehicleService;
import org.jboss.resteasy.reactive.MultipartForm;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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

    @GET
    @Path("/get/{vehicleType}")
    public Response getAllVehicles(
        @PathParam("vehicleType") String vehicleType
    ) {
       try {
            List<Vehicles> vehicles = vehicleService.listAll(vehicleType);
            List<?> responseDTOs = (List<?>) apiMiddleware.manageVehicleTypeResponseDTO(vehicleType, vehicles);
            return Response.ok(responseDTOs).build();
       } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error retrieving vehicles: " + e.getMessage())
                           .build();
       }
    }

    @GET
    @Path("/download/{vehicleId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("vehicleId") String vehicleId){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gridFSService.downloadFile(vehicleId, outputStream);
        
        return Response.ok(outputStream.toByteArray())
            .header("Content-Disposition", "attachment; filename=\"" + vehicleId + "\"")
            .build();
    }

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

    @GET
    @Path("/get/search")
    public Response search(
        @BeanParam VehicleSearchDTO searchParams
    ) {
        try {
            var vehicles = vehicleService.searchVehicle(searchParams);
            return Response.ok(vehicles).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Searching error: " + e.getMessage())
                        .build();
        }
    }

    @POST
    @Path("/save/saveAllVehicles/{vehicleType}")
    @Transactional
    public Response saveAllVehicles(
        @PathParam("vehicleType") String vehicleType, 
        List<Vehicles> vehicles
    ) {
        try {
            var vehiclesRequestDTO = apiMiddleware.manageVehiclesTypeRequestDTO(vehicleType, (Vehicles) vehicles);
            int savedVehicles = vehicleService.saveMultipleVehicles(vehicleType, (List<Vehicles>) vehiclesRequestDTO);
            return Response.status(Response.Status.CREATED).entity(savedVehicles).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Error saving vehicle: " + e.getMessage())
                           .build();
        }
    }

    @POST
    @Path("/save/{vehicleType}/docs")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public Response saveVehicleWithDocs(
        @PathParam("vehicleType") String vehicleType,
        @MultipartForm VehicleDocumentRequestDTO data
    ){
        try {
            var vehicleRequestDTO = apiMiddleware.manageVehiclesTypeRequestDTO(vehicleType, data.vehicles);
            Vehicles savedVehicles = vehicleService.saveVehicleWithDocuments(vehicleType,(Vehicles) vehicleRequestDTO, data.file, data.filename, data.contentType);

            return Response.status(Response.Status.CREATED).entity(savedVehicles).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Erro ao salvar veículo e documentos: " + e.getMessage())
                .build();
        }
    }

    @DELETE
    @Path("/delete/{vehicleType}/{id}")
    @Transactional
    public Response deleteVehicle(
        @PathParam("vehicleType") String vehicleType, 
        @PathParam("id") UUID id
    ) {
        try {
            vehicleService.deleteById(vehicleType, id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/delete/{vehicleType}")
    @Transactional
    public Response deleteManyVehicles(
        @PathParam("vehicleType") String vehicleType, 
        List<UUID> id
    ) {
        try {
            vehicleService.deleteManyVehicles(vehicleType, id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/edit/{vehicleType}/{id}")
    @Transactional
    public Response editVehicle(
        @PathParam("vehicleType") String vehicleType,
        @PathParam("id") UUID id,
        Vehicles vehicle
    ){
        try {
            vehicleService.editVehicleInfo(vehicleType, id, vehicle);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
