package org.acme.model.Bikes;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BikesRepository implements PanacheRepository<Bikes> {
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
