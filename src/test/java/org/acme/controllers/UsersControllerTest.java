package org.acme.controllers;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.acme.dtos.UsersRequestDTO;
import org.acme.dtos.UsersResponseDTO;
import org.acme.enums.EUserRole;
import org.acme.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@QuarkusTest
public class UsersControllerTest {

    @InjectMock
    UserService userService;

    private UsersRequestDTO requestDTO;
    private UsersResponseDTO responseDTO;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        requestDTO = new UsersRequestDTO(
            testUserId,
            "John Doe",
            "john.doe@example.com",
            "password123",
            "12345678900",
            "123 Main St",
            "City",
            "State",
            "Country",
            "123456789",
            "987654321",
            "stripeAccountId",
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            LocalDate.of(1990, 1, 1),
            EUserRole.CLIENT
        );

        responseDTO = new UsersResponseDTO(
            testUserId,
            "John Doe",
            "john.doe@example.com",
            "password123",
            "12345678900",
            "123 Main St",
            "City",
            "State",
            "Country",
            "123456789",
            "987654321",
            "stripeAccountId",
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            LocalDate.of(1990, 1, 1),
            EUserRole.CLIENT
        );
    }

    @Test
    void getAllUsers_whenAdmin_shouldReturnUserList() {
        Mockito.when(userService.getAllUsers()).thenReturn(List.of(responseDTO));

        given()
            .when().get("/users/get")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", is(1))
            .body("[0].id", is(testUserId.toString()));
    }

    @Test
    void getAllUsers_whenNotAdmin_shouldReturnForbidden() {
        given()
            .when().get("/users/get")
            .then()
            .statusCode(403);
    }

    @Test
    void getAllUsers_whenUnauthenticated_shouldReturnUnauthorized() {
        given()
            .when().get("/users/get")
            .then()
            .statusCode(401);
    }

    @Test
    void getUserById_whenAdminAndUserExists_shouldReturnUser() {
        Mockito.when(userService.getUserById(testUserId)).thenReturn(responseDTO);

        given()
            .pathParam("id", testUserId)
            .when().get("/users/get/{id}")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", is(testUserId.toString()));
    }

    @Test
    void getUserById_whenAdminAndUserNotFound_shouldReturnNotFound() {
        UUID notFoundId = UUID.randomUUID();
        Mockito.when(userService.getUserById(notFoundId)).thenReturn(null);

        given()
            .pathParam("id", notFoundId)
            .when().get("/users/get/{id}")
            .then()
            .statusCode(404);
    }

    @Test
    void getUserById_whenNotAdmin_shouldReturnForbidden() {
        given()
            .pathParam("id", testUserId)
            .when().get("/users/get/{id}")
            .then()
            .statusCode(403);
    }

    @Test
    void addUser_withValidData_shouldReturnCreated() {
        Mockito.when(userService.createUser(any(UsersRequestDTO.class))).thenReturn(responseDTO);

        given()
            .contentType(ContentType.JSON)
            .body(requestDTO)
            .when().post("/users/save")
            .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", is(testUserId.toString()));
    }

    @Test
    void addUser_whenServiceThrowsException_shouldReturnInternalServerError() {
        Mockito.when(userService.createUser(any(UsersRequestDTO.class))).thenThrow(new RuntimeException("Database error"));

        given()
            .contentType(ContentType.JSON)
            .body(requestDTO)
            .when().post("/users/save")
            .then()
            .statusCode(500)
            .body(is("Error creating user: Database error"));
    }

    @Test
    void deleteUser_whenAdminAndUserExists_shouldReturnNoContent() {
        Mockito.doNothing().when(userService).deleteUser(testUserId);

        given()
            .pathParam("id", testUserId)
            .when().delete("/users/delete/{id}")
            .then()
            .statusCode(204);
    }

    @Test
    void deleteUser_whenAdminAndServiceThrowsException_shouldReturnNotFound() {
        Mockito.doThrow(new IllegalArgumentException("User not found")).when(userService).deleteUser(testUserId);

        given()
            .pathParam("id", testUserId)
            .when().delete("/users/delete/{id}")
            .then()
            .statusCode(404)
            .body(is("Error deleting user: User not found"));
    }

    @Test
    void deleteUser_whenNotAdmin_shouldReturnForbidden() {
        given()
            .pathParam("id", testUserId)
            .when().delete("/users/delete/{id}")
            .then()
            .statusCode(403);
    }

    @Test
    void editUser_withValidData_shouldReturnOk() {
        Mockito.when(userService.editUser(eq(testUserId), any(UsersRequestDTO.class))).thenReturn(responseDTO);

        given()
            .pathParam("id", testUserId)
            .contentType(ContentType.JSON)
            .body(requestDTO)
            .when().put("/users/put/{id}")
            .then()
            .statusCode(200);
    }

    @Test
    void editUser_whenUserNotFound_shouldReturnNotFound() {
        Mockito.when(userService.editUser(eq(testUserId), any(UsersRequestDTO.class)))
            .thenThrow(new IllegalArgumentException("User not found"));

        given()
            .pathParam("id", testUserId)
            .contentType(ContentType.JSON)
            .body(requestDTO)
            .when().put("/users/put/{id}")
            .then()
            .statusCode(404)
            .body(is("Error editing user: User not found"));
    }
}