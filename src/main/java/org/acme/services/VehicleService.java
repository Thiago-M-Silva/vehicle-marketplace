package org.acme.services;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Map;

import org.acme.abstracts.Vehicles;
import org.acme.model.Bikes.BikesRepository;
import org.acme.model.Boats.BoatsRepository;
import org.acme.model.Cars.CarsRepository;
import org.acme.model.Planes.PlanesRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VehicleService {
    private final BikesRepository bikesRepository = new BikesRepository();
    private final BoatsRepository boatsRepository = new BoatsRepository();
    private final CarsRepository carsRepository = new CarsRepository();
    private final PlanesRepository planesRepository = new PlanesRepository();

    private final Map<String, Object> repositoryMap = Map.of(
        "bikes", bikesRepository,
        "boats", boatsRepository,
        "cars", carsRepository,
        "planes", planesRepository
    );

    public Vehicles createVehicles(String vehicleType, Vehicles vehicles) {
        Object repo = repositoryMap.get(vehicleType);
        if (repo == null) return null;
        try {
            repo.getClass().getMethod("persist", vehicles.getClass()).invoke(repo, vehicles);
            return vehicles;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public List<Vehicles> getAllVehicles(String vehicleType) {
        Object repo = repositoryMap.get(vehicleType);
        if (repo == null) return null;
        try {
            List<?> list = (List<?>) repo.getClass().getMethod("listAll").invoke(repo);
            return new ArrayList<Vehicles>((List<Vehicles>) list);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public Vehicles getVehiclesById(UUID id, String vehicleType) {
        Object repo = repositoryMap.get(vehicleType);
        if (repo == null) return null;
        try {
            return (Vehicles) repo.getClass().getMethod("findById", UUID.class).invoke(repo, id);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public Vehicles deleteVehicle(UUID id, String vehicleType) {
        Object repo = repositoryMap.get(vehicleType);
        if(repo == null) return null;
        try{
            return (Vehicles) repo.getClass().getMethod("deleteById", UUID.class).invoke(repo, id);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
