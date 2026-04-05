// src/lib/keycloak.ts
import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "http://localhost:8081",
  realm: "marketplace",
  clientId: "frontend",
});

export default keycloak;