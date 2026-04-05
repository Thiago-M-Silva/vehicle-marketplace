import keycloak from './lib/keycloak';
import { useState, useEffect } from 'react';
import './App.css'
import { AppRoutes } from './routes/appRoutes'

export const App = () => {
  const [initialized, setInitialized] = useState(false);

  useEffect(() => {
    keycloak
      .init({
        onLoad: "check-sso", // NÃO força login
        pkceMethod: "S256",
      })
      .then(() => {
        setInitialized(true);
      });
  }, []);

  setInterval(() => {
    keycloak.updateToken(30).catch(() => {
      keycloak.logout();
    });
  }, 10000);

  if (!initialized) return <p>Loading...</p>;

  return (
    <AppRoutes />
  )
}

export default App;