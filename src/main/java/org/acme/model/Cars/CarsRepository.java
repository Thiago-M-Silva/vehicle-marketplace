package org.acme.model.Cars;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CarsRepository implements PanacheRepository<Cars> {
    // @Inject
    // PanacheRepository<Bikes> bikesRepository;
    // @Override
    // public List<Bikes> listAll() {
    //     return bikesRepository.listAll();
    // }
    // @Override
    // public void persist(Bikes bike) {
    //     bikesRepository.persist(bike);
    // }
    // @Override
    // public void delete(Bikes bike) {
    //     bikesRepository.delete(bike);
    // }

}
