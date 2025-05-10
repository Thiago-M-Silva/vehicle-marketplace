package org.acme.controllers;

import java.util.List;
import java.util.UUID;

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
    @Transactional
    public Bikes addBike(BikesRequestDTO bike) {
        return vehicleService.createBikes(bike);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void deleteBike(@PathParam("id") UUID id) {
        vehicleService.deleteBikes(id);
    }
}
