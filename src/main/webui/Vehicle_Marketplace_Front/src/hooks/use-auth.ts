import keycloak from "@/lib/keycloak";
import type {
  KeycloakLoginOptions,
  KeycloakLogoutOptions,
  KeycloakTokenParsed,
} from "keycloak-js";
import {
  createElement,
  createContext,
  useContext,
  useEffect,
  useState,
  type ReactNode,
} from "react";

type AuthContextValue = {
  initialized: boolean;
  isAuthenticated: boolean;
  token?: string;
  login: (options?: KeycloakLoginOptions) => Promise<void>;
  logout: (options?: KeycloakLogoutOptions) => Promise<void>;
  getUser: () => KeycloakTokenParsed | undefined;
};

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

let keycloakInitPromise: Promise<boolean> | null = null;
const KEYCLOAK_INIT_TIMEOUT_MS = 8000;

const initializeKeycloak = () => {
  if (!keycloakInitPromise) {
    const initPromise = keycloak.init({
      onLoad: "check-sso",
      pkceMethod: "S256",
      silentCheckSsoRedirectUri: `${location.origin}/silent-check-sso.html`,
    });

    const timeoutPromise = new Promise<boolean>((resolve) => {
      window.setTimeout(() => {
        console.warn(
          `Keycloak init timed out after ${KEYCLOAK_INIT_TIMEOUT_MS}ms.`,
        );
        resolve(false);
      }, KEYCLOAK_INIT_TIMEOUT_MS);
    });

    keycloakInitPromise = Promise.race([initPromise, timeoutPromise]);
  }

  return keycloakInitPromise;
};

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [initialized, setInitialized] = useState(false);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [token, setToken] = useState<string | undefined>(undefined);

  const syncAuthState = () => {
    setIsAuthenticated(!!keycloak.authenticated);
    setToken(keycloak.token);
    setInitialized(true);
  };

  useEffect(() => {
    let isMounted = true;

    const syncIfMounted = () => {
      if (isMounted) {
        syncAuthState();
      }
    };

    keycloak.onReady = syncIfMounted;
    keycloak.onAuthSuccess = syncIfMounted;
    keycloak.onAuthRefreshSuccess = syncIfMounted;
    keycloak.onAuthLogout = syncIfMounted;
    keycloak.onAuthError = syncIfMounted;
    keycloak.onAuthRefreshError = syncIfMounted;
    keycloak.onTokenExpired = () => {
      keycloak
        .updateToken(30)
        .then(syncIfMounted)
        .catch(() => {
          keycloak.clearToken();
          syncIfMounted();
        });
    };

    initializeKeycloak()
      .then(syncIfMounted)
      .catch((error) => {
        console.error("Failed to initialize Keycloak", error);
        if (isMounted) {
          setInitialized(true);
          setIsAuthenticated(false);
          setToken(undefined);
        }
      });

    return () => {
      isMounted = false;
      keycloak.onReady = undefined;
      keycloak.onAuthSuccess = undefined;
      keycloak.onAuthRefreshSuccess = undefined;
      keycloak.onAuthLogout = undefined;
      keycloak.onAuthError = undefined;
      keycloak.onAuthRefreshError = undefined;
      keycloak.onTokenExpired = undefined;
    };
  }, []);

  return createElement(
    AuthContext.Provider,
    {
      value: {
        initialized,
        isAuthenticated,
        token,
        login: (options) => keycloak.login(options),
        logout: (options) =>
          keycloak.logout({
            redirectUri: window.location.origin,
            ...options,
          }),
        getUser: () => keycloak.tokenParsed,
      },
    },
    children,
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }

  return context;
};
