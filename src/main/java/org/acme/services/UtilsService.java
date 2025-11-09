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

    /**
     * Transfers ownership of a vehicle from a seller to a buyer.
     *
     * <p>This method locates the seller by their Stripe account ID and the buyer by their email,
     * then looks up the vehicle by its ID and type. If the vehicle's storage flag is 0, the method
     * will:
     * <ul>
     *   <li>remove the vehicle from the seller's corresponding collection (e.g. getBikes(), getCars(), etc.),</li>
     *   <li>add the vehicle to the buyer's corresponding collection,</li>
     *   <li>set the vehicle's storage flag to 1, and</li>
     *   <li>set the vehicle's owner to the buyer.</li>
     * </ul>
     *
     * <p>Accepted vehicleType values (case-insensitive) include: "bikes", "boats", "cars", "planes".
     * If the vehicle's storage flag is not 0, no transfer is performed.
     *
     * <p>Note: This method mutates the in-memory entity objects (Users and the Vehicle). Persistence
     * of these changes must be handled by the surrounding transactional/contextual layer (e.g. by the
     * calling service or repository).
     *
     * @param sellerStripeId the Stripe account identifier of the seller; must not be null
     * @param buyerEmail the email address of the buyer; must not be null
     * @param vehicleId the UUID of the vehicle to transfer; must not be null
     * @param vehicleType the type/category of the vehicle (e.g. "bikes", "boats", "cars", "planes"); case-insensitive
     * @throws IllegalArgumentException if sellerStripeId, buyerEmail, or vehicleId is null
     * @throws IllegalArgumentException if vehicleType is not one of the supported types
     * @throws NullPointerException if no seller, buyer, or vehicle entity is found for the provided identifiers
     */
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
