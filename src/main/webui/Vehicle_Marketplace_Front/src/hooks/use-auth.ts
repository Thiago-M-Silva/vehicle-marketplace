import keycloak from "@/lib/keycloak";

export const useAuth = () => {
  return {
    isAuthenticated: keycloak.authenticated,
    token: keycloak.token,

    login: () => keycloak.login(),

    logout: () =>
      keycloak.logout({
        redirectUri: window.location.origin,
      }),

    getUser: () => keycloak.tokenParsed,
  };
};