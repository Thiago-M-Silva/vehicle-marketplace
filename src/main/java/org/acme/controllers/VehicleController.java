package org.acme.controllers;

import java.util.List;
import java.util.UUID;

import org.acme.abstracts.Vehicles;
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

@Path("/vehicles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleController {

    @Inject
    VehicleService vehicleService;

    @GET
    @Path("/{vehicleType}")
    public List<? extends Vehicles> getAllVehicles(@PathParam("vehicleType") String vehicleType) {
        return vehicleService.listAll(vehicleType);
    }

    @GET
    @Path("/{vehicleType}/{id}")
    public Vehicles getVehiclesById(@PathParam("vehicleType") String vehicleType, @PathParam("id") UUID id) {
        return vehicleService.findById(vehicleType, id);
    }

    @POST
    @Path("/{vehicleType}")
    @Transactional
    public Vehicles addVehicle(@PathParam("vehicleType") String vehicleType, Vehicles vehicles) {
        return vehicleService.save(vehicleType, vehicles);
    }

    @DELETE
    @Path("/{vehicleType}/{id}")
    @Transactional
    public void deleteVehicle(@PathParam("vehicleType") String vehicleType, @PathParam("id") UUID id) {
        vehicleService.deleteById(vehicleType, id);
    }
}
