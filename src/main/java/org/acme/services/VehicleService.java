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
                bikesRepository.persist((Bikes) vehicles);
                return vehicles;
            
            case "boats":
                boatsRepository.persist((Boats) vehicles);
                return vehicles;

            case "cars":
                carsRepository.persist((Cars) vehicles);
                return vehicles;
            
            case "planes":
                planesRepository.persist((Planes) vehicles);
                return vehicles;

            default:
                break;
            }
            return null;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
            return null;
        }
    }

    // public List<Vehicles> getAllVehicles(String vehicleType) {
    //      try {
    //         switch (vehicleType) {
    //         case "bikes":
    //             return bikesRepository.listAll();
            
    //         case "boats":
    //             return boatsRepository.;

    //         case "cars":
    //             return carsRepository.;
            
    //         case "planes":
    //             return planesRepository.;

    //         default:
    //             break;
    //         }
    //         return null;
    //     } catch (Exception e) {
    //         // TODO: handle exception
    //         System.out.println(e);
    //         return null;
    //     }
    // }

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
