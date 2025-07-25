package org.acme.controllers;

import java.util.List;
import java.util.UUID;

import org.acme.abstracts.Vehicles;
import org.acme.middlewares.ApiMiddleware;
import org.acme.services.VehicleService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/vehicles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleController {

    @Inject
    VehicleService vehicleService;

    @Inject
    ApiMiddleware apiMiddleware;

    @GET
    @Path("/{vehicleType}")
    public List<?> getAllVehicles(@PathParam("vehicleType") String vehicleType) {
       try {
            List<Vehicles> vehicles = vehicleService.listAll(vehicleType);
            List<?> responseDTOs = apiMiddleware.manageVehicleTypeResponseDTO(vehicleType, vehicles);
            return (List<?>) Response.ok(responseDTOs).build();
       } catch (Exception e) {
            return (List<?>) Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error retrieving vehicles: " + e.getMessage())
                           .build();
       }
    }

    @GET
    @Path("/{vehicleType}/{id}")
    public Response getVehiclesById(@PathParam("vehicleType") String vehicleType, @PathParam("id") UUID id) {
        try {
            Vehicles vehicle = vehicleService.findById(vehicleType, id);
            return Response.ok(vehicle).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Vehicle not found: " + e.getMessage())
                           .build();
        }
    }

    @POST
    @Path("/{vehicleType}")
    @Transactional
    public Response addVehicle(@PathParam("vehicleType") String vehicleType, Vehicles vehicles) {
        try {
            var vehicleRequestDTO = apiMiddleware.manageVehicleTypeRequestDTO(vehicleType, vehicles);
            Vehicles savedVehicle = vehicleService.save(vehicleType, (Vehicles) vehicleRequestDTO);
            return Response.status(Response.Status.CREATED).entity(savedVehicle).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Error saving vehicle: " + e.getMessage())
                           .build();
        }
    }

    @DELETE
    @Path("/{vehicleType}/{id}")
    @Transactional
    public void deleteVehicle(@PathParam("vehicleType") String vehicleType, @PathParam("id") UUID id) {
        try {
            vehicleService.deleteById(vehicleType, id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting vehicle: " + e.getMessage());
        }
    }
}
