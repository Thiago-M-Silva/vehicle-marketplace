package org.acme.repositories;

import java.util.UUID;

import org.acme.model.Users;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsersRepository implements PanacheRepositoryBase<Users, UUID> {

}
