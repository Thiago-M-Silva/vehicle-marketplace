package org.acme.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.acme.dtos.UsersRequestDTO;
import org.acme.dtos.UsersResponseDTO;
import org.acme.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import jakarta.ws.rs.core.Response;

public class UsersControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UsersController usersController;

    private UUID testUserId;
    private UsersResponseDTO testUserResponse;
    private UsersRequestDTO testUserRequest;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testUserResponse = new UsersResponseDTO(
                testUserId,
                "Test User",
                "test@example.com",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        testUserRequest = new UsersRequestDTO(
                null,
                "Test User",
                "test@example.com",
                "password",
                null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    @Test
    void testGetAllUsersSuccess() {
        List<UsersResponseDTO> users = Arrays.asList(testUserResponse);
        when(userService.getAllUsers()).thenReturn(users);

        Response response = usersController.getAllUsers();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(users, response.getEntity());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetAllUsersException() {
        when(userService.getAllUsers()).thenThrow(new RuntimeException("Database error"));

        Response response = usersController.getAllUsers();

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error retrieving users"));
    }

    @Test
    void testGetUserByIdSuccess() {
        when(userService.getUserById(testUserId)).thenReturn(testUserResponse);

        Response response = usersController.getUserById(testUserId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(testUserResponse, response.getEntity());
        verify(userService, times(1)).getUserById(testUserId);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userService.getUserById(testUserId)).thenReturn(null);

        Response response = usersController.getUserById(testUserId);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("User not found"));
    }

    @Test
    void testGetUserByIdException() {
        when(userService.getUserById(testUserId)).thenThrow(new RuntimeException("Service error"));

        Response response = usersController.getUserById(testUserId);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error retrieving user"));
    }

    @Test
    void testAddUserSuccess() throws Exception {
        when(userService.createUser(testUserRequest)).thenReturn(testUserResponse);

        Response response = usersController.addUser(testUserRequest);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(testUserResponse, response.getEntity());
        verify(userService, times(1)).createUser(testUserRequest);
    }

    @Test
    void testAddUserException() throws Exception {
        when(userService.createUser(testUserRequest)).thenThrow(new RuntimeException("Creation failed"));

        Response response = usersController.addUser(testUserRequest);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error creating user"));
    }

    @Test
    void testDeleteUserSuccess() {
        doNothing().when(userService).deleteUser(testUserId);

        Response response = usersController.deleteUser(testUserId);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(userService, times(1)).deleteUser(testUserId);
    }

    @Test
    void testDeleteUserException() {
        doThrow(new RuntimeException("User not found")).when(userService).deleteUser(testUserId);

        Response response = usersController.deleteUser(testUserId);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error deleting user"));
    }

    @Test
    void testEditUserSuccess() {
        doNothing().when(userService).editUser(testUserId, testUserRequest);

        Response response = usersController.editUser(testUserId, testUserRequest);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(userService, times(1)).editUser(testUserId, testUserRequest);
    }

    @Test
    void testEditUserException() {
        doThrow(new RuntimeException("Update failed")).when(userService).editUser(testUserId, testUserRequest);

        Response response = usersController.editUser(testUserId, testUserRequest);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error editing user"));
    }

    @Test
    void testSetUserAsSellerSuccess() throws Exception {
        doNothing().when(userService).onboardSeller(testUserId);
        when(userService.generateOnboardingLink(testUserId)).thenReturn("http://onboarding.link");

        Response response = usersController.setUserAsSeller(testUserId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("http://onboarding.link", response.getEntity());
        verify(userService, times(1)).onboardSeller(testUserId);
        verify(userService, times(1)).generateOnboardingLink(testUserId);
    }

    @Test
    void testSetUserAsSellerException() throws Exception {
        doThrow(new RuntimeException("User not found")).when(userService).onboardSeller(testUserId);

        Response response = usersController.setUserAsSeller(testUserId);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error setting user as seller"));
    }
}
