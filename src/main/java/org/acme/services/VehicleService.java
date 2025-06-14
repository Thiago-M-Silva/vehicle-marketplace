package org.acme.services;

import java.util.List;
import java.util.UUID;

import org.acme.abstracts.Vehicles;
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

    @Inject BikesRepository bikesRepository;
    @Inject CarsRepository carsRepository;
    @Inject BoatsRepository boatsRepository;
    @Inject PlanesRepository planesRepository;

    public PanacheRepositoryBase<? extends Vehicles, UUID> getRepository(String vehicleType) {
        return switch (vehicleType.toLowerCase()) {
            case "bikes" -> bikesRepository;
            case "cars"  -> carsRepository;
            case "boats" -> boatsRepository;
            case "planes"-> planesRepository;
            default      -> throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
        };
    }

    public Vehicles save(String type, Vehicles vehicle) {
        switch (type.toLowerCase()) {
            case "bikes" -> ((BikesRepository) bikesRepository).persist((Bikes) vehicle);
            case "cars"  -> ((CarsRepository) carsRepository).persist((Cars) vehicle);
            case "boats" -> ((BoatsRepository) boatsRepository).persist((Boats) vehicle);
            case "planes"-> ((PlanesRepository) planesRepository).persist((Planes) vehicle);
            default      -> throw new IllegalArgumentException("Unknown vehicle type: " + type);
        }
        return vehicle;
    }

    public List<? extends Vehicles> listAll(String type) {
        return getRepository(type).listAll();
    }

    public Vehicles findById(String type, UUID id) {
        return getRepository(type).findById(id);
    }

    public boolean deleteById(String type, UUID id) {
        return getRepository(type).deleteById(id);
    }
}