package db.seeding;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.InputStream;
import java.util.List;
import org.acme.abstracts.Vehicles;
import org.acme.model.VehicleDocuments;
import org.acme.repositories.BikesRepository;
import org.acme.repositories.BoatsRepository;
import org.acme.repositories.CarsRepository;
import org.acme.repositories.PlanesRepository;
import org.acme.repositories.VehicleDocumentsRepository;
import org.acme.services.VehicleService;

@Startup
@ApplicationScoped
public class MongoSeederPanache {

    @Inject
    VehicleService vehicleService;

    @Inject
    BikesRepository bikesRepository;

    @Inject
    BoatsRepository boatsRepository;

    @Inject
    CarsRepository carsRepository;

    @Inject
    PlanesRepository planesRepository;

    @Inject
    VehicleDocumentsRepository vehicleDocumentsRepository;

    @PostConstruct
    void init() {
        seedMongo();
    }

    @Transactional
    public void seedMongo() {
        try {
            seedVehicleImages(bikesRepository.listAll(), "/files/bike.jpg", "bike", "jpg", "image/jpeg");
            seedVehicleImages(boatsRepository.listAll(), "/files/boat.jpg", "boat", "jpg", "image/jpeg");
            seedVehicleImages(carsRepository.listAll(), "/files/car.jpg", "car", "jpg", "image/jpeg");
            seedVehicleImages(planesRepository.listAll(), "/files/plane.jpeg", "plane", "jpeg", "image/jpeg");

            System.out.println("MongoDB seeded with vehicle documents (Panache)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void seedVehicleImages(
            List<? extends Vehicles> vehicles,
            String resourcePath,
            String filePrefix,
            String fileExtension,
            String contentType
    ) {
        for (Vehicles vehicle : vehicles) {
            if (vehicleDocumentsRepository.count("vehicleId", vehicle.getId().toString()) > 0) {
                continue;
            }

            try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
                if (is == null) {
                    System.err.println("Resource not found: " + resourcePath);
                    return;
                }

                String filename = "%s-%s.%s".formatted(filePrefix, vehicle.getId(), fileExtension);
                VehicleDocuments file = vehicleService.saveDocument(vehicle.getId(), filename, contentType, is);
                System.out.println("Uploaded " + filePrefix + " image with ID: " + file.id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
