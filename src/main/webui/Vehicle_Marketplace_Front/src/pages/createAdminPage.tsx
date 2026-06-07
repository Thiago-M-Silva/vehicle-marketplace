import { useEffect, useState } from "react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { IUser } from "@/interfaces/userInteface";
import { EUserRoles } from "@/enums/ERoles";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { useLocation, useNavigate, useSearchParams } from "react-router";
import { createUser } from "@/services/requests/usersRequests";
import { useAuth } from "@/hooks/use-auth";

export const CreateAdminPage = () => {
  const [searchParams] = useSearchParams();
  const location = useLocation();
  const navigate = useNavigate();
  const { login, initialized, isAuthenticated, getUser } = useAuth();
  const from = (location.state as { from?: string } | null)?.from || "/";

  const [confirmPassword, setConfirmPassword] = useState<string>("");
  const [userRole, setUserRole] = useState<EUserRoles>(EUserRoles.TECHNICAL);

  const [registerData, setRegisterData] = useState<IUser>({
    name: "",
    email: "",
    password: "",
    phoneNumber: "",
    address: "",
    city: "",
    state: "",
    country: "",
    cpf: "", //federal register document
    rg: "", //register document
    birthDate: new Date(),
    userType: userRole,
    profileImage: "",
    keycloakId: "",
    id: "",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleUserRole = (userRole: EUserRoles) => {
    setUserRole(userRole);
    setRegisterData((prev) => ({
      ...prev,
      userType: userRole,
    }));
  };

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

  useEffect(() => {
    if (initialized && isAuthenticated) {
      const authUser = getUser() as
        | {
            sub?: string;
            email?: string;
            name?: string;
            preferred_username?: string;
          }
        | undefined;

      if (!sessionStorage.getItem("currentUser")) {
        sessionStorage.setItem(
          "currentUser",
          JSON.stringify({
            id: authUser?.sub || "",
            keycloakId: authUser?.sub || "",
            email: authUser?.email || "",
            name: authUser?.name || authUser?.preferred_username || "",
          }),
        );
      }

      navigate(from, { replace: true });
    }
  }, [from, getUser, initialized, isAuthenticated, navigate]);

  const handleRegister = async () => {
    setError("");

    if (
      !registerData.email ||
      !registerData.password ||
      !registerData.name ||
      !registerData.phoneNumber ||
      !registerData.address ||
      !registerData.city ||
      !registerData.state ||
      !registerData.country ||
      !registerData.cpf ||
      !registerData.rg ||
      !registerData.birthDate
    ) {
      setError("Please fill in all required fields.");
      return;
    }

    if (registerData.password !== confirmPassword) {
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
        userType: userRole,
        keycloakId: "",
        profileImage: registerData.profileImage,
      };

      const createdUser = await createUser({
        ...fixedRegisterData,
        userRole: userRole,
      });
      const userWithoutPassword = {
        ...fixedRegisterData,
        ...createdUser,
      };
      delete userWithoutPassword.password;

      sessionStorage.setItem(
        "currentUser",
        JSON.stringify(userWithoutPassword),
      );

      await login({
        redirectUri: `${window.location.origin}${from}`,
      });
    } catch (err: unknown) {
      const requestError = err as {
        response?: { data?: { errorMessage?: string } };
        errorMessage?: string;
        status?: number;
      };
      const apiError =
        requestError.response?.data?.errorMessage || requestError.errorMessage;

      if (apiError) {
        setError(apiError);
      } else if (requestError.status === 409) {
        setError("User already exists with this email.");
      } else {
        setError(
          requestError.errorMessage ||
            "An unexpected error occurred during registration.",
        );
      }
    } finally {
      setLoading(false);
    }
  };

  const updateField = (field: string, value: string) => {
    setRegisterData((prev) => ({
      ...prev,
      [field]: value,
    }));
  };

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
          <div className="space-y-6 max-h-[500px] overflow-y-auto pr-4">
                {/* Personal Information */}
                <div>
                  <h3 className="text-sm font-semibold text-slate-900 mb-3">
                    Personal Information
                  </h3>
                  <div className="space-y-3 p-4 bg-slate-50 rounded-lg">
                    <div>
                      <label className="block text-xs font-medium text-slate-700 mb-1">
                        Full Name *
                      </label>
                      <Input
                        placeholder="John Doe"
                        value={registerData.name}
                        onChange={(e) => updateField("name", e.target.value)}
                        className="text-sm"
                      />
                    </div>

                    <div className="grid grid-cols-2 gap-3">
                      <div>
                        <label className="block text-xs font-medium text-slate-700 mb-1">
                          Birth Date *
                        </label>
                        <Input
                          type="date"
                          value={String(registerData.birthDate)}
                          onChange={(e) =>
                            updateField("birthDate", e.target.value)
                          }
                          className="text-sm"
                        />
                      </div>
                      <div>
                        <label className="block text-xs font-medium text-slate-700 mb-1">
                          Phone *
                        </label>
                        <Input
                          type="tel"
                          placeholder="(555) 123-4567"
                          value={registerData.phoneNumber}
                          onChange={(e) =>
                            updateField("phoneNumber", e.target.value)
                          }
                          className="text-sm"
                        />
                      </div>
                    </div>

                    <div className="grid grid-cols-2 gap-3">
                      <div>
                        <label className="block text-xs font-medium text-slate-700 mb-1">
                          CPF *
                        </label>
                        <Input
                          type="text"
                          placeholder="000.000.000-00"
                          value={registerData.cpf}
                          onChange={(e) => updateField("cpf", e.target.value)}
                          className="text-sm"
                        />
                      </div>
                      <div>
                        <label className="block text-xs font-medium text-slate-700 mb-1">
                          RG *
                        </label>
                        <Input
                          type="text"
                          placeholder="00.000.000-0"
                          value={registerData.rg}
                          onChange={(e) => updateField("rg", e.target.value)}
                          className="text-sm"
                        />
                      </div>
                    </div>
                  </div>
                </div>

                {/* Location Information */}
                <div>
                  <h3 className="text-sm font-semibold text-slate-900 mb-3">
                    Location
                  </h3>
                  <div className="space-y-3 p-4 bg-slate-50 rounded-lg">
                    <div>
                      <label className="block text-xs font-medium text-slate-700 mb-1">
                        Address *
                      </label>
                      <Input
                        type="text"
                        placeholder="123 Main Street"
                        value={registerData.address}
                        onChange={(e) => updateField("address", e.target.value)}
                        className="text-sm"
                      />
                    </div>

                    <div className="grid grid-cols-2 gap-3">
                      <div>
                        <label className="block text-xs font-medium text-slate-700 mb-1">
                          City *
                        </label>
                        <Input
                          type="text"
                          placeholder="New York"
                          value={registerData.city}
                          onChange={(e) => updateField("city", e.target.value)}
                          className="text-sm"
                        />
                      </div>
                      <div>
                        <label className="block text-xs font-medium text-slate-700 mb-1">
                          State *
                        </label>
                        <Input
                          type="text"
                          placeholder="NY"
                          value={registerData.state}
                          onChange={(e) => updateField("state", e.target.value)}
                          className="text-sm"
                        />
                      </div>
                    </div>

                    <div>
                      <label className="block text-xs font-medium text-slate-700 mb-1">
                        Country *
                      </label>
                      <Input
                        type="text"
                        placeholder="United States"
                        value={registerData.country}
                        onChange={(e) => updateField("country", e.target.value)}
                        className="text-sm"
                      />
                    </div>
                  </div>
                </div>

                {/* Account Information */}
                <div>
                  <h3 className="text-sm font-semibold text-slate-900 mb-3">
                    Account Details
                  </h3>
                  <div className="space-y-3 p-4 bg-slate-50 rounded-lg">
                    <div>
                      <label className="block text-xs font-medium text-slate-700 mb-1">
                        Email *
                      </label>
                      <Input
                        placeholder="you@example.com"
                        type="email"
                        value={registerData.email}
                        onChange={(e) => updateField("email", e.target.value)}
                        className="text-sm"
                      />
                    </div>

                    <div>
                      <label className="block text-xs font-medium text-slate-700 mb-1">
                        Password *
                      </label>
                      <Input
                        placeholder="••••••••"
                        type="password"
                        value={registerData.password}
                        onChange={(e) =>
                          updateField("password", e.target.value)
                        }
                        className="text-sm"
                      />
                    </div>

                    <div>
                      <label className="block text-xs font-medium text-slate-700 mb-1">
                        Confirm Password *
                      </label>
                      <Input
                        placeholder="••••••••"
                        type="password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        className="text-sm"
                      />
                    </div>
                  </div>
                </div>

                {/* User Role */}
                <div>
                  <label className="block text-xs font-medium text-slate-700 mb-1">
                    User Role *
                  </label>
                  <select
                    name="userRole"
                    id="userRole"
                    value={userRole}
                    onChange={(e) =>
                      handleUserRole(Number(e.target.value) as EUserRoles)
                    }
                    className="w-full h-9 rounded-md border border-input bg-transparent px-3 py-1 text-sm shadow-xs outline-none transition-[color,box-shadow] focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px]"
                  >
                    <option value={EUserRoles.CLIENT}>Client</option>
                    <option value={EUserRoles.TECHNICAL}>Technical</option>
                    <option value={EUserRoles.ADMIN}>Admin</option>
                  </select>
                </div>

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
        </CardContent>
      </Card>
    </div>
  );
};
