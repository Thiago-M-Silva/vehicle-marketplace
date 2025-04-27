package org.acme.model.Planes;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlanesRepository implements PanacheRepository<Planes> {
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
