package org.acme.controllers;

import java.util.List;
import java.util.UUID;

import org.acme.abstracts.Vehicles;
import org.acme.model.Bikes.Bikes;
import org.acme.model.Bikes.BikesRequestDTO;
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
    // TODO: test this controller and verify if service is working as expected

    @Inject
    VehicleService vehicleService;

    @GET
    public List<Bikes> getAllBikes() {
        return vehicleService.getAllBikes();
    }

     @GET
     @Path("/{id}")
    public Bikes getAllBikes(@PathParam("id") UUID id) {
        return vehicleService.getBikesById(id);
    }

    @POST
    @Path("/{vehicleType}")
    @Transactional
    public Vehicles addBike(@PathParam("vehicleType") String vehicleType, Vehicles vehicles) {
        return vehicleService.createVehicles(vehicleType, vehicles);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void deleteBike(@PathParam("id") UUID id) {
        vehicleService.deleteBikes(id);
    }
}
