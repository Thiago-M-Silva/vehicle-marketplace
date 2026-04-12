import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { useAuth } from "@/hooks/use-auth";
import { VehicleResumeSection } from "@/sections/vehicleResumeSection";
import type { KeycloakTokenParsed } from "keycloak-js";

// Mock user data for display
const MOCK_USER = {
  username: "johndoe",
  fullName: "John Doe",
  email: "john.doe@example.com",
  phone: "+1 (555) 123-4567",
  address: "1234 Main St",
  city: "New York",
  state: "NY",
  country: "USA",
  zip: "10001",
  cpf: "123.456.789-00",
  rg: "12.345.678-9",
  birthDate: "1990-01-01",
  avatar: "https://github.com/shadcn.png",
};

export const ProfilePage = () => {
  const { logout, getUser } = useAuth();
  const authUser = getUser() as
    | (KeycloakTokenParsed & {
        email?: string;
        name?: string;
        preferred_username?: string;
      })
    | undefined;

  const profileData = {
    ...MOCK_USER,
    username: authUser?.preferred_username || MOCK_USER.username,
    fullName: authUser?.name || authUser?.preferred_username || MOCK_USER.fullName,
    email: authUser?.email || MOCK_USER.email,
  };

  return (
    <div className="min-h-screen bg-slate-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="container mx-auto max-w-6xl space-y-8">
        {/* Header / Title */}
        <div>
          <h1 className="text-3xl font-bold tracking-tight text-slate-900">
            My Profile
          </h1>
          <p className="text-slate-600 mt-2">
            Manage your account settings and view your vehicles.
          </p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Left Column: Profile Card */}
          <Card className="lg:col-span-1 shadow-sm border-slate-200 h-fit">
            <CardHeader className="flex flex-col items-center text-center pb-2">
              <div className="w-32 h-32 rounded-full overflow-hidden border-4 border-slate-100 mb-4 shadow-sm">
                <img
                  src={profileData.avatar}
                  alt={profileData.username}
                  className="w-full h-full object-cover"
                />
              </div>
              <CardTitle className="text-2xl font-bold text-slate-900">
                {profileData.fullName}
              </CardTitle>
              <CardDescription className="text-sm font-medium">
                @{profileData.username}
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4 pt-4">
              <Button className="w-full">Edit Profile</Button>
              <Button
                variant="outline"
                className="w-full text-red-600 hover:text-red-700 hover:bg-red-50 border-red-200"
                onClick={() => void logout()}
              >
                Log Out
              </Button>
            </CardContent>
          </Card>

          {/* Right Column: Details */}
          <Card className="lg:col-span-2 shadow-sm border-slate-200">
            <CardHeader>
              <CardTitle>Personal Information</CardTitle>
              <CardDescription>
                View and update your personal details.
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
                {[
                  { label: "Email", value: profileData.email },
                  { label: "Phone", value: profileData.phone },
                  { label: "Birth Date", value: profileData.birthDate },
                  { label: "CPF", value: profileData.cpf },
                  { label: "RG", value: profileData.rg },
                ].map((item) => (
                  <div key={item.label} className="space-y-1">
                    <label className="text-sm font-medium text-slate-500">
                      {item.label}
                    </label>
                    <p className="text-base font-medium text-slate-900">
                      {item.value}
                    </p>
                  </div>
                ))}
              </div>

              <div className="mt-8 pt-6 border-t border-slate-100">
                <h3 className="text-lg font-semibold text-slate-900 mb-4">
                  Address
                </h3>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
                  <div className="space-y-1 sm:col-span-2">
                    <label className="text-sm font-medium text-slate-500">
                      Street Address
                    </label>
                    <p className="text-base font-medium text-slate-900">
                      {profileData.address}
                    </p>
                  </div>
                  {[
                    { label: "City", value: profileData.city },
                    { label: "State", value: profileData.state },
                    { label: "Country", value: profileData.country },
                    { label: "Zip Code", value: profileData.zip },
                  ].map((item) => (
                    <div key={item.label} className="space-y-1">
                      <label className="text-sm font-medium text-slate-500">
                        {item.label}
                      </label>
                      <p className="text-base font-medium text-slate-900">
                        {item.value}
                      </p>
                    </div>
                  ))}
                </div>
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Vehicles Section */}
        <Card className="shadow-sm border-slate-200 overflow-hidden">
          <CardHeader className="border-b border-slate-100 bg-white">
            <CardTitle>My Vehicles</CardTitle>
            <CardDescription>
              Vehicles you have listed for sale or rent.
            </CardDescription>
          </CardHeader>
          <CardContent className="p-0">
            <VehicleResumeSection />
          </CardContent>
        </Card>
      </div>
    </div>
  );
};
