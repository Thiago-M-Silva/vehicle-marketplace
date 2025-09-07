package org.acme.controllers;

import java.util.List;
import java.util.UUID;

import org.acme.dtos.UsersRequestDTO;
import org.acme.dtos.UsersResponseDTO;
import org.acme.model.Users;
import org.acme.services.UserService;

import jakarta.annotation.security.PermitAll;
// import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersController {

    @Inject
    UserService userService;

    @GET
    // @RolesAllowed("admin")
    public List<UsersResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GET
    @Path("/{id}")
    // @RolesAllowed("admin")
    public UsersResponseDTO getUserById(@PathParam("id") UUID id) {
        return userService.getUserById(id);
    }

    @POST
    @Transactional
    @PermitAll
    public Users addUser(UsersRequestDTO data) {
        return userService.createUser(data);
    }

    @DELETE
    @Path("/{id}")
    // @RolesAllowed("admin")
    @Transactional
    public void deleteUser(@PathParam("id") UUID id) {
        userService.deleteUser(id);
    }

    @PUT
    @Path("/{id}")
    @PermitAll
    public Response editUser(UsersRequestDTO user){
        try {
            userService.editUser(user);
            return Response.ok().build();
        } catch (Exception e) {
           return Response.serverError().build();
        }
    }
}