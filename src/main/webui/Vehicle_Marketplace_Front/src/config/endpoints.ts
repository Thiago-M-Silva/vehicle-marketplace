const env = import.meta.env;

const currentOrigin = window.location.origin;
const currentHost = window.location.hostname;
const currentProtocol = window.location.protocol;

// In production, the React bundle and the API are served by the same Quarkus
// application. VITE_* values remain available for split/local deployments.
export const BACKEND_BASIC_URL =
  env.VITE_BACKEND_BASIC_URL || currentOrigin;
export const BACKEND_VEHICLE_URL =
  env.VITE_BACKEND_VEHICLE_URL || `${currentOrigin}/vehicles`;
export const BACKEND_USERS_URL =
  env.VITE_BACKEND_USERS_URL || `${currentOrigin}/users`;

export const KEYCLOAK_URL =
  env.VITE_KEYCLOAK_URL || `${currentProtocol}//${currentHost}:8081`;
export const KEYCLOAK_REALM = env.VITE_KEYCLOAK_REALM || "marketplace";
export const KEYCLOAK_CLIENT_ID = env.VITE_KEYCLOAK_CLIENT_ID || "frontend";
