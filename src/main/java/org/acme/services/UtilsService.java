package org.acme.services;

import org.acme.model.Users;

import java.util.UUID;

import org.acme.model.Bikes;
import org.acme.model.Boats;
import org.acme.model.Cars;
import org.acme.model.Planes;

import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UtilsService {
    @Inject UserService userService;
    @Inject VehicleService vehicleService;

    public void updateOwner(String sellerStripeId, String buyerEmail, UUID vehicleId, String vehicleType){
        if (sellerStripeId == null || buyerEmail == null || vehicleId == null){
            throw new IllegalArgumentException("Seller, buyer or vehicle cannot be null");
        }

        System.out.println("updateOwner called");

        Users seller = userService.usersRepository.find("stripeAccountId LIKE ?1", sellerStripeId).firstResult();
        Users buyer = userService.usersRepository.find("email LIKE ?1", buyerEmail).firstResult();
        var vehicle = vehicleService.findById(vehicleType, vehicleId);

        if (vehicle.getStorage() == 0){
            switch (vehicleType.toLowerCase()) {
                case "bikes" -> { 
                    Bikes bike = (Bikes) vehicle;

                    seller.getBikes().remove(bike); 
                    buyer.getBikes().add(bike);
                    bike.setStorage(1);
                    bike.setOwner(buyer);
                }
                case "boats" -> { 
                    Boats boat = (Boats) vehicle;

                    seller.getBoats().remove(boat); 
                    buyer.getBoats().add(boat);
                    boat.setStorage(1);
                    boat.setOwner(buyer);
                 }
                case "cars" -> { 
                    Cars car = (Cars) vehicle;

                    seller.getCars().remove(car); 
                    buyer.getCars().add(car);
                    car.setStorage(1);
                    car.setOwner(buyer);
                 }
                case "planes" -> { 
                    Planes plane = (Planes) vehicle;

                    seller.getPlanes().remove(plane); 
                    buyer.getPlanes().add(plane);
                    plane.setStorage(1);
                    plane.setOwner(buyer);
                 }
                default -> throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);

            }
        }
    }

}
