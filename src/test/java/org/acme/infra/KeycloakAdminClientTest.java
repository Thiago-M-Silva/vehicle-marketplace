
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.ws.rs.core.Response;



package org.acme.infra;




@ExtendWith(MockitoExtension.class)
class KeycloakAdminClientTest {

    @Mock
    RealmResource realmResource;

    @Mock
    UsersResource usersResource;

    @Mock
    UserResource userResource;

    @Mock
    Keycloak mockKeycloak;

    @Mock
    Response response;

    @Test
    void createUser_success_createsUserAndResetsPassword() throws Exception {
        // Arrange
        KeycloakBuilder mockBuilder = mock(KeycloakBuilder.class);
        when(mockBuilder.serverUrl(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.realm(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.grantType(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.clientId(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.username(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.password(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.build()).thenReturn(mockKeycloak);

        try (MockedStatic<KeycloakBuilder> mocked = mockStatic(KeycloakBuilder.class)) {
            mocked.when(KeycloakBuilder::builder).thenReturn(mockBuilder);

            when(mockKeycloak.realm(anyString())).thenReturn(realmResource);
            when(realmResource.users()).thenReturn(usersResource);

            when(response.getStatus()).thenReturn(201);
            when(response.getHeaderString("Location")).thenReturn("http://localhost/auth/admin/realms/test/users/abc-123");
            when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);
            when(usersResource.get("abc-123")).thenReturn(userResource);

            KeycloakAdminClient client = new KeycloakAdminClient();
            // package-private fields - set directly
            client.serverUrl = "http://localhost";
            client.realm = "test";
            client.adminUsername = "admin";
            client.adminPassword = "pass";

            // Act
            String userId = client.createUser("John", "john@example.com", "password123");

            // Assert
            assertEquals("abc-123", userId);

            ArgumentCaptor<UserRepresentation> userCaptor = ArgumentCaptor.forClass(UserRepresentation.class);
            verify(usersResource).create(userCaptor.capture());
            UserRepresentation created = userCaptor.getValue();
            assertEquals("john@example.com", created.getUsername());
            assertEquals("john@example.com", created.getEmail());
            assertEquals("John", created.getFirstName());
            assertEquals(Boolean.TRUE, created.isEnabled());

            ArgumentCaptor<CredentialRepresentation> credCaptor = ArgumentCaptor.forClass(CredentialRepresentation.class);
            verify(userResource).resetPassword(credCaptor.capture());
            CredentialRepresentation cred = credCaptor.getValue();
            assertEquals(CredentialRepresentation.PASSWORD, cred.getType());
            assertEquals("password123", cred.getValue());
            assertEquals(Boolean.FALSE, cred.isTemporary());
        }
    }

    @Test
    void createUser_failure_throwsRuntimeExceptionWithErrorBody() throws Exception {
        // Arrange
        KeycloakBuilder mockBuilder = mock(KeycloakBuilder.class);
        when(mockBuilder.serverUrl(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.realm(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.grantType(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.clientId(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.username(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.password(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.build()).thenReturn(mockKeycloak);

        try (MockedStatic<KeycloakBuilder> mocked = mockStatic(KeycloakBuilder.class)) {
            mocked.when(KeycloakBuilder::builder).thenReturn(mockBuilder);

            when(mockKeycloak.realm(anyString())).thenReturn(realmResource);
            when(realmResource.users()).thenReturn(usersResource);

            when(response.getStatus()).thenReturn(400);
            when(response.getStatusInfo()).thenReturn(Response.Status.BAD_REQUEST);
            when(response.readEntity(String.class)).thenReturn("{\"error\":\"bad request\"}");
            when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);

            KeycloakAdminClient client = new KeycloakAdminClient();
            client.serverUrl = "http://localhost";
            client.realm = "test";
            client.adminUsername = "admin";
            client.adminPassword = "pass";

            // Act & Assert
            RuntimeException ex = assertThrows(RuntimeException.class, ()
                    -> client.createUser("Jane", "jane@example.com", "pw")
            );
            // basic assertion that error message contains status info and the error body
            String msg = ex.getMessage();
            // should contain status info and the error JSON
            boolean containsStatus = msg.contains("BAD_REQUEST");
            boolean containsBody = msg.contains("{\"error\":\"bad request\"}");
            if (!containsStatus || !containsBody) {
                throw new AssertionError("Exception message missing expected content: " + msg);
            }
        }
    }
}
