package db.seeding;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.acme.enums.ECategory;
import org.acme.enums.EColors;
import org.acme.enums.EFuelType;
import org.acme.enums.EStatus;
import org.acme.enums.EUserRole;
import org.acme.model.Bikes;
import org.acme.model.Boats;
import org.acme.model.Cars;
import org.acme.model.Planes;
import org.acme.model.Users;
import org.acme.repositories.BikesRepository;
import org.acme.repositories.BoatsRepository;
import org.acme.repositories.CarsRepository;
import org.acme.repositories.PlanesRepository;
import org.acme.repositories.UsersRepository;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Startup
@ApplicationScoped
public class PostgresSeeding {
    @Inject BikesRepository bikesRepository;
    @Inject BoatsRepository boatsRepository;
    @Inject CarsRepository carsRepository;
    @Inject PlanesRepository planesRepository;
    @Inject UsersRepository usersRepository;

    @PostConstruct
    void init(){
        seedDatabase();
    }
    
    @Transactional
    public void seedDatabase() {
        Users seller;

        if(usersRepository.count() == 0){
            usersRepository.persist(createSeedUser(
                "Admin",
                "admin@email.com",
                "admin1234",
                "556299999997",
                "11111111111",
                "1111111",
                LocalDate.of(1990, 1, 15),
                EUserRole.ADMIN,
                null,
                "cus_seed_admin",
                "keycloak-seed-admin"
            ));

            usersRepository.persist(createSeedUser(
                "Jose",
                "jose@email.com",
                "jose1234",
                "556299999999",
                "22222222222",
                "2222222",
                LocalDate.of(1995, 5, 20),
                EUserRole.CLIENT,
                null,
                "cus_seed_jose",
                "keycloak-seed-jose"
            ));

            seller = createSeedSeller();
            usersRepository.persist(seller);
        } else {
            seller = getSeedSeller();
        }

        if(bikesRepository.count() == 0) {
            // Seed bikes
            Bikes bike = new Bikes();
            Bikes bike1 = new Bikes();
            Bikes bike2 = new Bikes();

            bike.setName("Ninja_ZX-6R");
            bike.setBrand("Kawasaki");
            bike.setYear(2022);
            bike.setModel("ZX-6R");
            bike.setHorsepower(128);
            bike.setTransmissionType("Manual");
            bike.setDescription("Sport bike with great performance");
            bike.setStorage(2);
            bike.setVehicleStatus(EStatus.NEW);
            bike.setCategory(ECategory.SPORTIVE);
            bike.setColor(EColors.GREEN);
            bike.setPrice(BigDecimal.valueOf(85000.00));
            bike.setFuelType(EFuelType.ETHANOL);
            bike.setOwner(seller);

            bike1.setName("Harley-Davidson Street 750");
            bike1.setBrand("Harley-Davidson");
            bike1.setYear(2021);
            bike1.setModel("Street 750");
            bike1.setHorsepower(53);
            bike1.setTransmissionType("Manual");
            bike1.setDescription("Cruiser bike with classic design");
            bike1.setStorage(1);
            bike1.setVehicleStatus(EStatus.USED);
            bike1.setCategory(ECategory.CRUISERS);
            bike1.setColor(EColors.BLACK);
            bike1.setFuelType(EFuelType.GASOLINE);
            bike1.setOwner(seller);
            bike1.setPrice(BigDecimal.valueOf(40000.00));

            bike2.setName("Factor 150 Standard");
            bike2.setBrand("Yamaha");
            bike2.setYear(2025);
            bike2.setModel("Factor 150");
            bike2.setHorsepower(150);
            bike2.setTransmissionType("Automatic");
            bike2.setDescription("Amazing sport bike");
            bike2.setStorage(2);
            bike2.setVehicleStatus(EStatus.NEW);
            bike2.setCategory(ECategory.SPORTIVE);
            bike2.setColor(EColors.RED);
            bike2.setFuelType(EFuelType.GASOLINE);
            bike2.setOwner(seller);
            bike2.setPrice(BigDecimal.valueOf(5000.00));

            bikesRepository.persist(bike);
            bikesRepository.persist(bike1);
            bikesRepository.persist(bike2);
        }

        if(boatsRepository.count() == 0) {
            // Seed boats
            Boats boat = new Boats();
            Boats boat1 = new Boats();
            Boats boat2 = new Boats();

            boat.setName("Sea Ray 280");
            boat.setBrand("Sea Ray");
            boat.setYear(2021);
            boat.setModel("280 SLX");
            boat.setHorsepower(350);
            boat.setTransmissionType("Automatic");
            boat.setDescription("Luxury speedboat");
            boat.setStorage(1);
            boat.setPrice(BigDecimal.valueOf(85000.00));
            boat.setVehicleStatus(EStatus.NEW);
            boat.setCategory(ECategory.DAYCRUISER);
            boat.setColor(EColors.RED);
            boat.setFuelType(EFuelType.GASOLINE);
            boat.setOwner(seller);
            boat.setNumberOfCabins(2);

            boat1.setName("Bayliner_VR5");
            boat1.setBrand("Bayliner");
            boat1.setYear(2023);
            boat1.setModel("VR5_Bowrider");
            boat1.setHorsepower(250);
            boat1.setTransmissionType("Automatic");
            boat1.setDescription("Perfect_boat_for_family_leisure");
            boat1.setStorage(2);
            boat1.setPrice(BigDecimal.valueOf(45000.00));
            boat1.setVehicleStatus(EStatus.REFORMED);
            boat1.setCategory(ECategory.BOW_RIDER);
            boat1.setColor(EColors.YELLOW);
            boat1.setFuelType(EFuelType.GASOLINE);  
            boat1.setOwner(seller);
            boat1.setNumberOfCabins(1);

            boat2.setName("Princess_V50");
            boat2.setBrand("Princess_Yachts");
            boat2.setYear(2022);
            boat2.setModel("V50");
            boat2.setHorsepower(480);
            boat2.setTransmissionType("Automatic");
            boat2.setDescription("end_luxury_yacht");
            boat2.setStorage(2);
            boat2.setPrice(BigDecimal.valueOf(350000.00));
            boat2.setVehicleStatus(EStatus.USED);
            boat2.setCategory(ECategory.ALUMINIUM);
            boat2.setColor(EColors.WHITE);
            boat2.setFuelType(EFuelType.GASOLINE);
            boat2.setOwner(seller);
            boat2.setNumberOfCabins(3);

            boatsRepository.persist(boat);
            boatsRepository.persist(boat1);
            boatsRepository.persist(boat2);
        }

        if(carsRepository.count() == 0) {
            // Seed cars
            Cars car = new Cars();
            Cars car1 = new Cars();
            Cars car2 = new Cars();

            car.setName("Model S");
            car.setBrand("Tesla");
            car.setYear(2022);
            car.setModel("Performance");
            car.setHorsepower(670);
            car.setTransmissionType("Automatic");
            car.setDescription("Electric sedan with autopilot");
            car.setStorage(1);
            car.setVehicleStatus(EStatus.NEW);
            car.setCategory(ECategory.SEDAN);
            car.setColor(EColors.WHITE);
            car.setFuelType(EFuelType.ELECTRIC);
            car.setOwner(seller);
            car.setPrice(BigDecimal.valueOf(79999.99));
            car.setRentalPriceMonthly(BigDecimal.valueOf(500));

            car1.setName("Creta");
            car1.setBrand("Hyundai");
            car1.setYear(2025);
            car1.setModel("1.0");
            car1.setHorsepower(320);
            car1.setTransmissionType("Automatic");
            car1.setDescription("Our newest and amazing car");
            car1.setStorage(2);
            car1.setVehicleStatus(EStatus.NEW);
            car1.setCategory(ECategory.SUV);
            car1.setColor(EColors.BLACK);
            car1.setFuelType(EFuelType.GASOLINE);
            car1.setOwner(seller);
            car1.setPrice(BigDecimal.valueOf(55000.00));

            car2.setName("Onix");
            car2.setBrand("Chevrolet");
            car2.setYear(2025);
            car2.setModel("1.0 Flex");
            car2.setHorsepower(400);
            car2.setTransmissionType("Manual");
            car2.setDescription("Last Chevrolet announcement, a fast and counfortable sedan");
            car2.setStorage(3);
            car2.setVehicleStatus(EStatus.NEW);
            car2.setCategory(ECategory.SEDAN);
            car2.setColor(EColors.WHITE);
            car2.setFuelType(EFuelType.GASOLINE);
            car2.setOwner(seller);
            car2.setPrice(BigDecimal.valueOf(60000.00));

            carsRepository.persist(car);
            carsRepository.persist(car1);
            carsRepository.persist(car2);
        }

        if(planesRepository.count() == 0) {
            // Seed planes
            Planes plane = new Planes();
            Planes plane1 = new Planes();
            Planes plane2 = new Planes();
            
            plane.setName("Cessna 172");
            plane.setBrand("Cessna");
            plane.setYear(2019);
            plane.setModel("Skyhawk");
            plane.setHorsepower(180);
            plane.setTransmissionType("Manual");
            plane.setDescription("Most popular training aircraft");
            plane.setStorage(2);
            plane.setVehicleStatus(EStatus.NEW);
            plane.setCategory(ECategory.JET);
            plane.setColor(EColors.WHITE);
            plane.setFuelType(EFuelType.DIESEL);
            plane.setOwner(seller);
            plane.setPrice(BigDecimal.valueOf(320000.00));

            plane1.setName("Cirrus SR22");
            plane1.setBrand("Cirrus");
            plane1.setYear(2024);
            plane1.setModel("SR22T G6");
            plane1.setHorsepower(310);
            plane1.setTransmissionType("Automatic");
            plane1.setDescription("Modern single-engine plane with parachute system");
            plane1.setStorage(1);
            plane1.setVehicleStatus(EStatus.REFORMED);
            plane1.setCategory(ECategory.TWIN_ENGINE);
            plane1.setColor(EColors.WHITE);
            plane1.setFuelType(EFuelType.JET_FUEL);
            plane1.setOwner(seller);
            plane1.setPrice(BigDecimal.valueOf(750000.00));

            plane2.setName("Piper PA-28");
            plane2.setBrand("Piper");
            plane2.setYear(2022);
            plane2.setModel("Archer LX");
            plane2.setHorsepower(180);
            plane2.setTransmissionType("Manual");
            plane2.setDescription("Reliable trainer aircraft");
            plane2.setStorage(2);
            plane2.setVehicleStatus(EStatus.USED);
            plane2.setCategory(ECategory.SINGLE_ENGINE);
            plane2.setColor(EColors.WHITE);
            plane2.setFuelType(EFuelType.PROPANE);
            plane2.setOwner(seller);
            plane2.setPrice(BigDecimal.valueOf(280000.00));

            planesRepository.persist(plane);
            planesRepository.persist(plane1);
            planesRepository.persist(plane2);
        }

        System.out.println("PostgresSQL seeded with users and vehicles");
    }

    private Users getSeedSeller() {
        Users seller = usersRepository.find("email", "seller@email.com").firstResult();

        if (seller == null) {
            seller = createSeedSeller();
            usersRepository.persist(seller);
        }

        return seller;
    }

    private Users createSeedSeller() {
        return createSeedUser(
            "Seller",
            "seller@email.com",
            "seller1234",
            "556299999998",
            "33333333333",
            "3333333",
            LocalDate.of(1988, 8, 10),
            EUserRole.CLIENT,
            "acct_seed_seller",
            "cus_seed_seller",
            "keycloak-seed-seller"
        );
    }

    private Users createSeedUser(
        String name,
        String email,
        String password,
        String phoneNumber,
        String cpf,
        String rg,
        LocalDate birthDate,
        EUserRole userRole,
        String stripeAccountId,
        String stripeCustomerId,
        String keycloakId
    ) {
        Users user = new Users();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        user.setAddress("Example address");
        user.setCity("Example");
        user.setState("Example");
        user.setCountry("Brazil");
        user.setCpf(cpf);
        user.setRg(rg);
        user.setBirthDate(birthDate);
        user.setUserRole(userRole);
        user.setStripeAccountId(stripeAccountId);
        user.setStripeCustomerId(stripeCustomerId);
        user.setKeycloakId(keycloakId);
        return user;
    }
}
