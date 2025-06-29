package org.acme.repositories;

import java.util.UUID;

import org.acme.model.Boats;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BoatsRepository implements PanacheRepositoryBase<Boats, UUID> {
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
