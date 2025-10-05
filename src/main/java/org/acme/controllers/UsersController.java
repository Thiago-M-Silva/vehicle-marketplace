package org.acme.controllers;

import java.util.List;
import java.util.UUID;

import org.acme.dtos.UsersRequestDTO;
import org.acme.dtos.UsersResponseDTO;
import org.acme.services.UserService;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
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

    @Inject UserService userService;

    /**
     * Retrieves a list of all users.
     * <p>
     * This endpoint is accessible only to users with the "admin" role.
     * </p>
     *
     * @return a {@link Response} containing a list of {@link UsersResponseDTO} objects if successful,
     *         or an error message with HTTP 500 status if an exception occurs.
     */
    @GET
    @Path("/get")
    @RolesAllowed("admin")
    public Response getAllUsers() {
        try {
            List<UsersResponseDTO> users = userService.getAllUsers();
            return Response.ok(users).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error retrieving users: " + e.getMessage())
                           .build();
        }
    }


    /**
     * Retrieves a user by their unique identifier.
     * <p>
     * This endpoint is accessible only to users with the "admin" role.
     * </p>
     *
     * @param id the UUID of the user to retrieve
     * @return a {@link Response} containing the user data if found, or an appropriate error message if not found or if an error occurs
     */
    @GET
    @Path("/get/{id}")
    @RolesAllowed("admin")
    public Response getUserById(@PathParam("id") UUID id) {
        try {
            UsersResponseDTO user = userService.getUserById(id);
            if (user != null) {
                return Response.ok(user).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("User not found with ID: " + id)
                               .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error retrieving user: " + e.getMessage())
                           .build();
        }
    }

    /**
     * Handles HTTP POST requests to create a new user.
     * <p>
     * This endpoint is accessible to all users and expects a {@link UsersRequestDTO}
     * object containing the user details in the request body. If the user is successfully
     * created, it returns a {@link UsersResponseDTO} with HTTP status 201 (Created).
     * In case of an error, it returns an error message with HTTP status 500 (Internal Server Error).
     *
     * @param data the user details to be created
     * @return a Response containing the created user or an error message
     */
    @POST
    @Path("/save")
    @PermitAll
    public Response addUser(UsersRequestDTO data) {
        try {
            UsersResponseDTO createdUser = userService.createUser(data);
            return Response.status(Response.Status.CREATED).entity(createdUser).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error creating user: " + e.getMessage())
                           .build();
        }
    }

    /**
     * Deletes a user with the specified UUID.
     * <p>
     * This endpoint is accessible only to users with the "admin" role.
     * If the user is successfully deleted, a 204 No Content response is returned.
     * If the user is not found or an error occurs, a 404 Not Found response is returned with an error message.
     *
     * @param id the UUID of the user to delete
     * @return a Response indicating the outcome of the delete operation
     */
    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed("admin")
    public Response deleteUser(@PathParam("id") UUID id) {
        try {
            userService.deleteUser(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Error deleting user: " + e.getMessage())
                           .build();
        }
    }

    /**
     * Updates the user information for the specified user ID.
     *
     * @param id   the UUID of the user to be updated
     * @param user the user data to update, encapsulated in a {@link UsersRequestDTO}
     * @return a {@link Response} indicating the outcome of the update operation:
     *         - 200 OK if the update was successful
     *         - 404 NOT FOUND with an error message if the user could not be found or updated
     */
    @PUT
    @Path("/put/{id}")
    @PermitAll
    public Response editUser(@PathParam("id") UUID id, UsersRequestDTO user){
        try {
            userService.editUser(id, user);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Error editing user: " + e.getMessage())
                           .build();
        }
    }
}