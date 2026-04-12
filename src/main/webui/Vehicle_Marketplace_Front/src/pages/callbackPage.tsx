// import { useEffect } from "react";

// export const CallbackPage = () => {
//   const {
//     VITE_KEYCLOAK_URL,
//     VITE_KEYCLOAK_REALM,
//     VITE_KEYCLOAK_REDIRECT_URI,
//     VITE_KEYCLOAK_CLIENT_ID,
//   } = import.meta.env;

//   useEffect(() => {
//     const searchParams = new URLSearchParams(window.location.search);
//     const code = searchParams.get("code");
//     const errorParam = searchParams.get("error");

//     if (errorParam) {
//       console.error("Keycloak Error:", errorParam);
//       window.location.href = `/enter?error=${errorParam}`;
//       return;
//     }

//     if (!code) return;

//     fetch(
//       `${VITE_KEYCLOAK_URL}/realms/${VITE_KEYCLOAK_REALM}/protocol/openid-connect/token`,
//       {
//         method: "POST",
//         headers: {
//           "Content-Type": "application/x-www-form-urlencoded",
//         },
//         body: new URLSearchParams({
//           grant_type: "authorization_code",
//           client_id: VITE_KEYCLOAK_CLIENT_ID,
//           code: code,
//           redirect_uri: VITE_KEYCLOAK_REDIRECT_URI,
//         }),
//       },
//     )
//       .then((res) => {
//         if (!res.ok) {
//           return res.json().then((errData) => {
//             throw new Error(
//               `Token exchange failed: ${errData.error_description || res.statusText}`,
//             );
//           });
//         }
//         return res.json();
//       })
//       .then((data) => {
//         if (data.access_token) {
//           localStorage.setItem("access_token", data.access_token);

//           const destination =
//             sessionStorage.getItem("redirectAfterLogin") || "/";
//           sessionStorage.removeItem("redirectAfterLogin");
//           window.location.href = destination;
//         }
//       })
//       .catch((err) => {
//         console.error("Login Error:", err);
//         window.location.href = "/enter?error=auth_failed";
//       });
//   }, []);

//   return <p>Logging in...</p>;
// };
