import { useEffect } from "react";

export const CallbackPage = () => {
  useEffect(() => {
    const code = new URLSearchParams(window.location.search).get("code");

    if (!code) return;

    fetch("http://localhost:8081/realms/myrealm/protocol/openid-connect/token", {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: new URLSearchParams({
        grant_type: "authorization_code",
        client_id: "frontend",
        code: code!,
        redirect_uri: "http://localhost:5173/callback",
      }),
    })
      .then(res => res.json())
      .then(data => {
        localStorage.setItem("access_token", data.access_token);
        window.location.href = "/";
      });
  }, []);

  return <p>Logging in...</p>;
};