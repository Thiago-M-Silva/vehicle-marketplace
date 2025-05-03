package org.acme.controllers;

import java.util.List;

import org.acme.model.Bikes.Bikes;
import org.acme.model.Bikes.BikesRepository;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/vehicles")
public class VehicleController {

    @Inject
    BikesRepository bikesRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Bikes> getAllBikes() {
        return bikesRepository.listAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addBike(Bikes bike) {
        bikesRepository.persist(bike);
    }

    // @POST
    // @Consumes(MediaType.APPLICATION_JSON)
    // public void updateBike(Bikes bike) {
    //     return bikesRepository.update(null, null);
    // }


    @DELETE
    @Path("/{id}")
    public void deleteBike(@PathParam("id") Long id) {
        Bikes bike = bikesRepository.findById(id);
        if (bike != null) {
            bikesRepository.delete(bike);
        }
    }
}
