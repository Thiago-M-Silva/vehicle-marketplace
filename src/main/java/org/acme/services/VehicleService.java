package org.acme.services;

import java.util.List;
import java.util.UUID;

import org.acme.model.Bikes.Bikes;
import org.acme.model.Bikes.BikesRepository;
import org.acme.model.Bikes.BikesRequestDTO;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VehicleService {
    private final BikesRepository bikesRepository;

    public VehicleService(BikesRepository bikesRepository) {
        this.bikesRepository = bikesRepository;
    }

    public Bikes createBikes(BikesRequestDTO data) {
        Bikes bikes = new Bikes();
        bikesRepository.persist(bikes);
        return bikes;
    }

    public List<Bikes> getAllBikes() {
        return bikesRepository.listAll();
    }

    public Bikes getBikesById(UUID id) {
        return bikesRepository.findById(id);
    }

    public void deleteBikes(UUID id) {
        Bikes bikes = bikesRepository.findById(id);
        if (bikes != null) {
            bikesRepository.delete(bikes);
        }
    }
}
