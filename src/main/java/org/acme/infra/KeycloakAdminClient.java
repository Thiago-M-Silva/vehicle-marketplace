package org.acme.infra;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

@ApplicationScoped
public class KeycloakAdminClient {

    @ConfigProperty(name = "keycloak.admin.server-url")
    String serverUrl;

    @ConfigProperty(name = "quarkus.keycloak.devservices.realm-name")
    String realm;

    @ConfigProperty(name = "keycloak.admin.username")
    String adminUsername;

    @ConfigProperty(name = "keycloak.admin.password")
    String adminPassword;

    private Keycloak getInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl) // must NOT include /realms
                .realm("master")
                .grantType(OAuth2Constants.PASSWORD)
                .clientId("admin-cli")
                .username(adminUsername)
                .password(adminPassword)
                .build();
    }

    public String createUser(String name, String email, String password) {
        Keycloak keycloak = getInstance();

        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(email);
        user.setEmail(email);
        user.setFirstName(name);

        Response response = keycloak.realm(realm).users().create(user);

        if (response.getStatus() != 201) {
            String errorMsg;
            try {
                errorMsg = response.readEntity(String.class);
            } catch (Exception e) {
                errorMsg = "Unable to read error message.";
            }
            throw new RuntimeException(
                "Failed to create user: " + response.getStatusInfo() + "\n" + errorMsg
            );
        }

        String location = response.getHeaderString("Location");
        String userId = location.replaceAll(".*/([^/]+)$", "$1");

        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(password);
        credentials.setTemporary(false);

        keycloak.realm(realm).users().get(userId).resetPassword(credentials);
        return userId;
    }

}
