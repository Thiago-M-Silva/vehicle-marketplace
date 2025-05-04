package org.acme.model.Users;

import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsersRepository implements PanacheRepositoryBase<Users, UUID> {

}
