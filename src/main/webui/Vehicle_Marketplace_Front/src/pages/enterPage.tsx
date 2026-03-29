import { useState } from "react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  Tabs,
  TabsList,
  TabsTrigger,
  TabsContent,
} from "@radix-ui/react-tabs";

export const EnterPage = () => {
  // -------------------------
  // STATE
  // -------------------------
  const [registerData, setRegisterData] = useState({
    name: "",
    birthDate: "",
    email: "",
    password: "",
    confirmPassword: "",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  // -------------------------
  // CONFIG
  // -------------------------
  const KEYCLOAK_URL = "http://localhost:8081";
  const REALM = "marketplace";
  const CLIENT_ID = "frontend";
  const REDIRECT_URI = "http://localhost:5173/callback";

  // -------------------------
  // LOGIN (Keycloak Redirect)
  // -------------------------
  const handleLogin = () => {
    const params = new URLSearchParams({
      client_id: CLIENT_ID,
      response_type: "code",
      scope: "openid",
      redirect_uri: REDIRECT_URI,
    });

    // window.location.href = `${KEYCLOAK_URL}/realms/${REALM}/protocol/openid-connect/auth?${params.toString()}`;
    window.location.href = `${KEYCLOAK_URL}/realms/${REALM}/account`
  };

  // -------------------------
  // REGISTER (Your Backend)
  // -------------------------
  const handleRegister = async () => {
    setError("");

    if (registerData.password !== registerData.confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    try {
      setLoading(true);

      const res = await fetch("/api/users", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name: registerData.name,
          birthDate: registerData.birthDate,
          email: registerData.email,
          password: registerData.password,
        }),
      });

      if (!res.ok) {
        throw new Error("Failed to create account");
      }

      // 🔥 Auto login after register
      handleLogin();
    } catch (err: any) {
      setError(err.message || "Something went wrong");
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
      <Card className="w-full max-w-md shadow-lg">
        <CardHeader className="space-y-1 text-center">
          <CardTitle className="text-2xl font-bold">
            Welcome
          </CardTitle>
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
                <Button
                  className="w-full"
                  onClick={handleLogin}
                >
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
                  <p className="text-sm text-red-500 text-center">
                    {error}
                  </p>
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