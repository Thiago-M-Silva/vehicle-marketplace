package org.acme.services;

import java.util.List;
import java.util.UUID;

import org.acme.abstracts.Vehicles;
import org.acme.model.Bikes.Bikes;
import org.acme.model.Bikes.BikesRepository;
import org.acme.model.Bikes.BikesRequestDTO;
import org.acme.model.Boats.Boats;
import org.acme.model.Boats.BoatsRepository;
import org.acme.model.Cars.Cars;
import org.acme.model.Cars.CarsRepository;
import org.acme.model.Planes.Planes;
import org.acme.model.Planes.PlanesRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VehicleService {
    private final BikesRepository bikesRepository = new BikesRepository();
    private final BoatsRepository boatsRepository = new BoatsRepository();
    private final CarsRepository carsRepository = new CarsRepository();
    private final PlanesRepository planesRepository = new PlanesRepository();

    public Vehicles createVehicles(String vehicleType, Vehicles vehicles) {
        try {
            switch (vehicleType) {
            case "bikes":
                Bikes bikes = new Bikes();
                bikesRepository.persist(bikes);
                return bikes;
            
            case "boats":
                Boats boats = new Boats();
                boatsRepository.persist(boats);
                return boats;

            case "cars":
                Cars cars = new Cars();
                carsRepository.persist(cars);
                return cars;
            
            case "planes":
                Planes planes = new Planes();
                planesRepository.persist(planes);
                return planes;

            default:
                break;
            }
            return null;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
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
