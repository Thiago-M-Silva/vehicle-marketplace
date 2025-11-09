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
                .serverUrl(serverUrl) 
                .realm("master")
                .grantType(OAuth2Constants.PASSWORD)
                .clientId("admin-cli")
                .username(adminUsername)
                .password(adminPassword)
                .build();
    }

    /**
     * Creates a new user in Keycloak and sets their initial password.
     *
     * <p>Behaviour:
     * <ul>
     *   <li>Obtains a Keycloak client instance.</li>
     *   <li>Builds a UserRepresentation with the provided name and email (email used as username).</li>
     *   <li>Sends a create request to the configured realm.</li>
     *   <li>If the response status is not 201 (Created), logs the response and error body and throws a RuntimeException
     *       containing the status information and error message.</li>
     *   <li>On success, extracts the created user's ID from the Location header, creates a password credential
     *       (non-temporary) and resets the user's password.</li>
     * </ul>
     *
     * <p>Security note: the provided password is transmitted to Keycloak to set the user's credentials.
     * Ensure secure transport (HTTPS) and appropriate handling of sensitive data in callers.
     *
     * @param name the first name to assign to the new user (may be null or empty)
     * @param email the email address to assign and use as the username; must be unique within the realm
     * @param password the password to assign to the new user; it will be stored according to the realm's credential policies
     * @return the ID of the newly created user in Keycloak
     * @throws RuntimeException if the user creation request fails (non-201 response) or the error body cannot be read
     */
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
            System.out.println("Keycloak response: " + response.getStatus() + " " + response.getStatusInfo());
            System.out.println("Error body: " + response.readEntity(String.class));

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
