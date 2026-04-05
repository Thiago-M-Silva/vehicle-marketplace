import { useEffect, useState } from "react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { IUser } from "@/interfaces/userInteface";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Tabs, TabsList, TabsTrigger, TabsContent } from "@/components/ui/tabs";
import { useSearchParams } from "react-router";
import { createUser } from "@/services/requests/usersRequests";
import { useAuth } from "@/hooks/use-auth";

export const EnterPage = () => {
  const {
    VITE_KEYCLOAK_URL,
    VITE_KEYCLOAK_REALM,
    VITE_KEYCLOAK_CLIENT_ID,
    VITE_KEYCLOAK_REDIRECT_URI,
  } = import.meta.env;

  const [searchParams] = useSearchParams();
  const { login } = useAuth();

  const [registerData, setRegisterData] = useState({
    name: "",
    email: "",
    password: "",
    phoneNumber: "",
    address: "",
    city: "",
    state: "",
    country: "",
    cpf: "",
    rg: "",
    birthDate: "",
    confirmPassword: "",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  // Captura erros vindos do redirecionamento (Keycloak ou Callback)
  useEffect(() => {
    const urlError = searchParams.get("error");
    if (urlError) {
      const errorMessages: Record<string, string> = {
        auth_failed: "Failed to exchange token. Check console for details.",
        invalid_request: "Invalid request to login provider.",
        access_denied: "Access was denied.",
      };
      setError(errorMessages[urlError] || `Authentication error: ${urlError}`);
    }
  }, [searchParams]);

  const handleLogin = () => {
    if (!VITE_KEYCLOAK_CLIENT_ID || !VITE_KEYCLOAK_URL) {
      console.error("Keycloak environment variables are missing!");
      setError("System configuration error. Please contact admin.");
      return;
    }

    const params = new URLSearchParams({
      client_id: VITE_KEYCLOAK_CLIENT_ID,
      redirect_uri: VITE_KEYCLOAK_REDIRECT_URI,
      response_type: "code",
      scope: "openid",
      prompt: "login", // FORÇA a exibição da tela de login para teste
    });

    window.location.href = `${VITE_KEYCLOAK_URL}/realms/${VITE_KEYCLOAK_REALM}/protocol/openid-connect/auth?${params.toString()}`;
  };

  // -------------------------
  // REGISTER (Your Backend)
  // -------------------------
  const handleRegister = async () => {
    setError("");

    if (!registerData.email || !registerData.password || !registerData.name) {
      setError("Please fill in all required fields.");
      return;
    }

    if (registerData.password !== registerData.confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    try {
      setLoading(true);

      const fixedRegisterData: Partial<IUser> = {
        name: registerData.name,
        email: registerData.email,
        password: registerData.password,
        phoneNumber: registerData.phoneNumber,
        address: registerData.address,
        city: registerData.city,
        state: registerData.state,
        country: registerData.country,
        cpf: registerData.cpf,
        rg: registerData.rg,
        birthDate: new Date(registerData.birthDate),
        userType: 1,
      };

      await createUser(fixedRegisterData);
      handleLogin();
    } catch (err: any) {
      // Captura mensagens específicas do backend/Keycloak (como o 409 Conflict)
      const apiError = err.response?.data?.errorMessage || err.errorMessage;

      if (apiError) {
        setError(apiError);
      } else if (err.status === 409) {
        setError("User already exists with this email.");
      } else {
        setError(
          err.errorMessage ||
            "An unexpected error occurred during registration.",
        );
      }
    } finally {
      setLoading(false);
    }
  };

  // -------------------------
  // HANDLERS
  // -------------------------
  const updateField = (field: string, value: string) => {
    setRegisterData((prev) => ({
      ...prev,
      [field]: value,
    }));
  };

  // -------------------------
  // UI
  // -------------------------
  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-50 py-12 px-4">
      <Card className="w-full max-w-2xl shadow-lg">
        <CardHeader className="space-y-1 text-center">
          <CardTitle className="text-2xl font-bold">Welcome</CardTitle>
          <CardDescription>
            Enter your details to access your account
          </CardDescription>
        </CardHeader>

        <CardContent>
          <Tabs defaultValue="login" className="w-full">
            <TabsList className="grid w-full grid-cols-2 mb-8 bg-slate-100 p-1 rounded-lg">
              <TabsTrigger value="login">Login</TabsTrigger>
              <TabsTrigger value="register">Register</TabsTrigger>
            </TabsList>

            {/* ---------------- LOGIN ---------------- */}
            <TabsContent value="login">
              <div className="space-y-4">
                <Button className="w-full" onClick={() => login()}>
                  Sign in with Keycloak
                </Button>

                <p className="text-xs text-center text-slate-500">
                  You will be redirected to secure login
                </p>
              </div>
            </TabsContent>

            {/* ---------------- REGISTER ---------------- */}
            <TabsContent value="register">
              <div className="space-y-4">
                <Input
                  placeholder="Enter your name"
                  value={registerData.name}
                  onChange={(e) => updateField("name", e.target.value)}
                />

                <Input
                  type="date"
                  value={registerData.birthDate}
                  onChange={(e) => updateField("birthDate", e.target.value)}
                />

                <Input
                  placeholder="Enter your email"
                  type="email"
                  value={registerData.email}
                  onChange={(e) => updateField("email", e.target.value)}
                />

                <Input
                  placeholder="Enter your password"
                  type="password"
                  value={registerData.password}
                  onChange={(e) => updateField("password", e.target.value)}
                />

                <Input
                  placeholder="Confirm your password"
                  type="password"
                  value={registerData.confirmPassword}
                  onChange={(e) =>
                    updateField("confirmPassword", e.target.value)
                  }
                />

                {error && (
                  <div className="flex items-center gap-3 p-3 rounded-lg border border-red-200 bg-red-50 text-red-800 animate-in fade-in slide-in-from-top-1">
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="18"
                      height="18"
                      viewBox="0 0 24 24"
                      fill="none"
                      stroke="currentColor"
                      strokeWidth="2"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      className="shrink-0"
                    >
                      <circle cx="12" cy="12" r="10" />
                      <line x1="12" y1="8" x2="12" y2="12" />
                      <line x1="12" y1="16" x2="12.01" y2="16" />
                    </svg>
                    <span className="text-sm font-medium">{error}</span>
                  </div>
                )}

                <Button
                  className="w-full"
                  onClick={handleRegister}
                  disabled={loading}
                >
                  {loading ? "Creating account..." : "Create Account"}
                </Button>
              </div>
            </TabsContent>
          </Tabs>
        </CardContent>
      </Card>
    </div>
  );
};
