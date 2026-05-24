import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { useAuth } from "@/hooks/use-auth";
import { IUser } from "@/interfaces/userInteface";
import type { IVehicle } from "@/interfaces/vehiclesInteface";
import { getUserByKeycloakId } from "@/services/requests/usersRequests";
import { NotFountError } from "@/sections/notFoundErrorSection";
import type { KeycloakTokenParsed } from "keycloak-js";
import { useState, useEffect } from "react";

const currencyFormatter = new Intl.NumberFormat("en-US", {
  style: "currency",
  currency: "USD",
});

export const ProfilePage = () => {
  const { logout, getUser } = useAuth();
  const authUser = getUser() as
    | (KeycloakTokenParsed & {
        email?: string;
        name?: string;
        preferred_username?: string;
      })
    | undefined;

  const [profileData, setProfileData] = useState<IUser | null>(null);

  useEffect(() => {
    if (!authUser) return;
    const fetchUser = async (keycloakId: string) => {
      try {
        const user = await getUserByKeycloakId(keycloakId);
        setProfileData(user ?? null);
      } catch (e) {
        console.error("Failed to fetch user by keycloak id", e);
        setProfileData(null);
      }
    };

    void fetchUser(authUser.sub as string);
  }, [authUser]);

  if (!profileData) return <NotFountError />;

  const p = profileData as IUser;
  const userVehicles = [
    p.Bike ? { kind: "Bike", vehicle: p.Bike.info } : null,
    p.Boat ? { kind: "Boat", vehicle: p.Boat.info } : null,
    p.Car ? { kind: "Car", vehicle: p.Car.info } : null,
    p.Plane ? { kind: "Plane", vehicle: p.Plane.info } : null,
  ].filter(
    (item): item is { kind: string; vehicle: IVehicle } => item !== null,
  );

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
                  src={p.profileImage}
                  alt={p.name || p.email}
                  className="w-full h-full object-cover"
                />
              </div>
              <CardTitle className="text-2xl font-bold text-slate-900">
                {p.name}
              </CardTitle>
              <CardDescription className="text-sm font-medium">
                @{p.keycloakId || (p.email).split("@")[0]}
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
                  { label: "Email", value: p.email },
                  { label: "Phone", value: p.phoneNumber },
                  {
                    label: "Birth Date",
                    value: p.birthDate ? String(p.birthDate) : "",
                  },
                  { label: "CPF", value: p.cpf },
                  { label: "RG", value: p.rg },
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
                      {p.address}
                    </p>
                  </div>
                  {[
                    { label: "City", value: p.city },
                    { label: "State", value: p.state },
                    { label: "Country", value: p.country },
                    { label: "Zip Code", value: "" },
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
            {userVehicles.length > 0 ? (
              <div className="divide-y divide-slate-100">
                {userVehicles.map(({ kind, vehicle }) => (
                  <div
                    key={`${kind}-${vehicle.id}`}
                    className="grid grid-cols-1 md:grid-cols-[160px_1fr_auto] gap-4 p-6 items-center"
                  >
                    <div className="aspect-[4/3] overflow-hidden rounded-md bg-slate-100">
                      {vehicle.images?.[0] ? (
                        <img
                          src={vehicle.images[0]}
                          alt={vehicle.name}
                          className="h-full w-full object-cover"
                        />
                      ) : (
                        <div className="flex h-full w-full items-center justify-center text-sm font-medium text-slate-500">
                          No image
                        </div>
                      )}
                    </div>
                    <div className="min-w-0 space-y-2">
                      <div className="flex flex-wrap items-center gap-2">
                        <h3 className="text-lg font-semibold text-slate-900">
                          {vehicle.name}
                        </h3>
                        <span className="rounded bg-slate-100 px-2 py-1 text-xs font-medium text-slate-600">
                          {kind}
                        </span>
                      </div>
                      <p className="text-sm text-slate-600">
                        {[vehicle.brand, vehicle.model, vehicle.year]
                          .filter(Boolean)
                          .join(" - ")}
                      </p>
                      <p className="line-clamp-2 text-sm text-slate-500">
                        {vehicle.description}
                      </p>
                    </div>
                    <div className="text-left md:text-right">
                      <p className="text-lg font-bold text-slate-900">
                        {currencyFormatter.format(Number(vehicle.price ?? 0))}
                      </p>
                      <p className="text-sm text-slate-500">
                        {vehicle.vehicleStatus}
                      </p>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div className="p-6 text-center text-slate-500">
                No vehicles listed yet.
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  );
};
