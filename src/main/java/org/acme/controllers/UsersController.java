package org.acme.controllers;

import java.util.List;
import java.util.UUID;

import org.acme.model.Users.Users;
import org.acme.model.Users.UsersRequestDTO;
import org.acme.services.UserService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersController {

    @Inject
    UserService userService;

    @GET
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @GET
    @Path("/{id}")
    public Users getUserById(@PathParam("id") UUID id) {
        return userService.getUserById(id);
    }

    @POST
    @Transactional
    public Users addUser(UsersRequestDTO data) {
        return userService.createUser(data);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void deleteUser(@PathParam("id") UUID id) {
        userService.deleteUser(id);
    }
}
