package org.acme.services;

import org.acme.abstracts.Vehicles;
import org.acme.model.Users;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TradeService {
    public void BuyVehicle(Vehicles vehicle, Users buyer) {
        // TODO: 
        // [ ] 1. Check if the vehicle is available for sale
        // [ ] 2. Process payment (this is a placeholder, real payment processing would be more complex)
        // [ ] 3. Transfer ownership -> add vehicle to buyer's list and decrease its quantity
        // [ ] 4. Update trade table
        // [ ] 5. Notify both parties (buyer and seller) -> send email 
    }
}
