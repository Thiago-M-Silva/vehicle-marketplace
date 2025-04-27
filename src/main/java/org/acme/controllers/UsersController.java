package org.acme.controllers;

import java.util.List;

import org.acme.model.Users.Users;
import org.acme.model.Users.UsersRepository;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/users")
public class UsersController {
    @Inject
    UsersRepository usersRepository;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> getAllUsers() {
        return usersRepository.listAll();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addUser(Users user) {
        usersRepository.persist(user);
    }
    @DELETE
    @Path("/{id}")
    public void deleteUser(@PathParam("id") Long id) {
        Users user = usersRepository.findById(id);
        if (user != null) {
            usersRepository.delete(user);
        }
    }
}
