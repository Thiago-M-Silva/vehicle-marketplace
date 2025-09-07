package db.seeding;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import org.acme.model.Bikes;
import org.acme.model.Boats;
import org.acme.model.Cars;
import org.acme.model.Planes;
import org.acme.model.VehicleDocuments;
import org.acme.repositories.BikesRepository;
import org.acme.repositories.BoatsRepository;
import org.acme.repositories.CarsRepository;
import org.acme.repositories.PlanesRepository;
import org.acme.repositories.VehicleDocumentsRepository;
import org.acme.services.VehicleService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.io.InputStream;

@Startup
@ApplicationScoped
public class MongoSeederPanache {

    @Inject VehicleService vehicleService;

    @Inject BikesRepository bikesRepository;

    @Inject BoatsRepository boatsRepository;

    @Inject CarsRepository carsRepository;

    @Inject PlanesRepository planesRepository;

    @Inject VehicleDocumentsRepository vehicleDocumentsRepository;

    @PostConstruct
    void init() {
        seedMongo();
    }
    
    @Transactional
    public void seedMongo() {
        try {
            if (vehicleDocumentsRepository.count() == 0) {

                Bikes bikes = bikesRepository.find("name", "Ninja_ZX-6R").firstResult();
                Boats boats = boatsRepository.find("name", "Sea Ray 280").firstResult();
                Cars cars = carsRepository.find("name", "Model S").firstResult();
                Planes planes = planesRepository.find("name", "Boeing 747").firstResult();
                
                if (bikes != null) {
                    try (InputStream is = getClass().getResourceAsStream("db/seeding/files/motobike.jpg")) {
                        VehicleDocuments bikeFileId = vehicleService.saveDocument(bikes.getId(), "motobike.jpg", "image/jpeg", is);
                        System.out.println("Uploaded bike image with ID: " + bikeFileId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (boats != null) {
                    try (InputStream is = getClass().getResourceAsStream("db/seeding/files/boat.jpg")) {
                        VehicleDocuments boatFileId = vehicleService.saveDocument(boats.getId(), "boat.jpg", "image/jpeg", is);
                        System.out.println("Uploaded boat image with ID: " + boatFileId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (cars != null) {
                    try (InputStream is = getClass().getResourceAsStream("db/seeding/files/car.jpg")) {
                        VehicleDocuments carFileId = vehicleService.saveDocument(cars.getId(), "car.jpg", "image/jpeg", is);
                        System.out.println("Uploaded car image with ID: " + carFileId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (planes != null) {
                    try (InputStream is = getClass().getResourceAsStream("db/seeding/files/plane.jpg")) {
                        VehicleDocuments planeFileId = vehicleService.saveDocument(planes.getId(), "plane.jpg", "image/jpeg", is);
                        System.out.println("Uploaded plane image with ID: " + planeFileId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
                System.out.println("✅ MongoDB seeded with vehicle documents (Panache)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
