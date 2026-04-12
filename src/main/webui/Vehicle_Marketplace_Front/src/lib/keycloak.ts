import Keycloak from "keycloak-js";

const {
  VITE_KEYCLOAK_URL,
  VITE_KEYCLOAK_REALM,
  VITE_KEYCLOAK_CLIENT_ID
} = import.meta.env;

const keycloak = new Keycloak({
  url: VITE_KEYCLOAK_URL as string,
  realm: VITE_KEYCLOAK_REALM as string,
  clientId: VITE_KEYCLOAK_CLIENT_ID as string
});

export default keycloak;
