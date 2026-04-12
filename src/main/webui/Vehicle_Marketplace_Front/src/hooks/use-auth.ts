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
const KEYCLOAK_STORAGE_KEY = "vehicle-marketplace:keycloak-session";

type StoredKeycloakSession = {
  token?: string;
  refreshToken?: string;
  idToken?: string;
};

const loadStoredSession = (): StoredKeycloakSession => {
  const rawSession = window.localStorage.getItem(KEYCLOAK_STORAGE_KEY);

  if (!rawSession) {
    return {};
  }

  try {
    return JSON.parse(rawSession) as StoredKeycloakSession;
  } catch (error) {
    console.warn("Failed to parse stored Keycloak session.", error);
    window.localStorage.removeItem(KEYCLOAK_STORAGE_KEY);
    return {};
  }
};

const persistSession = () => {
  window.localStorage.setItem(
    KEYCLOAK_STORAGE_KEY,
    JSON.stringify({
      token: keycloak.token,
      refreshToken: keycloak.refreshToken,
      idToken: keycloak.idToken,
    }),
  );
};

const clearPersistedSession = () => {
  window.localStorage.removeItem(KEYCLOAK_STORAGE_KEY);
};

const initializeKeycloak = () => {
  if (!keycloakInitPromise) {
    const { token, refreshToken, idToken } = loadStoredSession();

    const initPromise = keycloak.init({
      onLoad: "check-sso",
      pkceMethod: "S256",
      token,
      refreshToken,
      idToken,
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
    const authenticated = !!keycloak.authenticated;

    if (authenticated && keycloak.token && keycloak.refreshToken) {
      persistSession();
    } else {
      clearPersistedSession();
    }

    setIsAuthenticated(authenticated);
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
          clearPersistedSession();
          syncIfMounted();
        });
    };

    initializeKeycloak()
      .then(syncIfMounted)
      .catch((error) => {
        console.error("Failed to initialize Keycloak", error);
        if (isMounted) {
          clearPersistedSession();
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
