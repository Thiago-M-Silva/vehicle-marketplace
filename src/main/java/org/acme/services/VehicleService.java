package org.acme.services;

import java.util.List;
import java.util.UUID;

import org.acme.abstracts.Vehicles;
import org.acme.middlewares.ApiMiddleware;
import org.acme.model.Bikes.Bikes;
import org.acme.model.Bikes.BikesRepository;
import org.acme.model.Boats.Boats;
import org.acme.model.Boats.BoatsRepository;
import org.acme.model.Cars.Cars;
import org.acme.model.Cars.CarsRepository;
import org.acme.model.Planes.Planes;
import org.acme.model.Planes.PlanesRepository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class VehicleService {
    @Inject 
    ApiMiddleware apiMiddleware;

    @Inject 
    BikesRepository bikesRepository;
    
    @Inject 
    CarsRepository carsRepository;
    
    @Inject 
    BoatsRepository boatsRepository;
    
    @Inject 
    PlanesRepository planesRepository;

    @SuppressWarnings("unchecked")
    private <T extends Vehicles> PanacheRepositoryBase<T, UUID> getRepository(String vehicleType) {
        return (PanacheRepositoryBase<T, UUID>) switch (vehicleType.toLowerCase()) {
            case "bikes" -> bikesRepository;
            case "cars"  -> carsRepository;
            case "boats" -> boatsRepository;
            case "planes"-> planesRepository;
            default      -> throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
        };
    }

    public <T extends Vehicles> T save(String type, T vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        var repository = getRepository(type);
        repository.persist(vehicle);
        return vehicle;
    }

    public List<Vehicles> listAll(String type) {
        try {
            List<Vehicles> vehicles = getRepository(type).listAll();
            return vehicles;
        } catch (Exception e) {
            throw new RuntimeException("Failed to list vehicles for type: " + type, e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Vehicles> T findById(String type, UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        var repository = getRepository(type);
        return (T) repository.findById(id);
    }

    public boolean deleteById(String type, UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        var repository = getRepository(type);
        return repository.deleteById(id);
    }
}