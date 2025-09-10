package org.acme.services;

import org.acme.abstracts.Vehicles;
import org.acme.model.Users;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


// TODO: 
// [ ] 1. Check if the vehicle is available for sale
// [ ] 2. Process payment (this is a placeholder, real payment processing would be more complex)
// [ ] 3. Transfer ownership -> add vehicle to buyer's list and decrease its quantity
// [ ] 4. Update trade table
// [ ] 5. Notify both parties (buyer and seller) -> send email  
@ApplicationScoped
public class TradeService {
    @Inject VehicleService vehicleService;
    
    public void BuyVehicle(Vehicles vehicle, String vehicleType, Users buyer) {
        try {
            // Step 1: Check if the vehicle is available for sale
            Vehicles tradeVehicle = vehicleService.findById(vehicleType, vehicle.getId());

            if (tradeVehicle == null) {
                throw new IllegalArgumentException("Vehicle is not available for sale.");
            }

            // Step 2: Process payment (placeholder)
            // processPayment(buyer, vehicle);

            // Step 3: Transfer ownership
            // buyer.getVehicles().add(vehicle);
            // vehicle.setQuantity(vehicle.getQuantity() - 1);

            // Step 4: Update trade table (placeholder)
            // updateTradeTable(vehicle, buyer);

            // Step 5: Notify both parties (placeholder)
            // notifyParties(vehicle, buyer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
