import { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { useNavigate } from "react-router";
import { getAllVehicleByKind } from "@/services/requests/vehiclesRequest";
import { IVehicle } from "@/interfaces/vehiclesInteface";
import {
  Card,
  CardHeader,
  CardTitle,
  CardContent,
  CardFooter,
} from "@/components/ui/card";
import { LoadingSection } from "@/sections/loadingSection";

type VehicleWithType = IVehicle & { type: string };

export const RentingPage = () => {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState("bikes");
  const [vehicles, setVehicles] = useState<VehicleWithType[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const vehicleKinds = ["bikes", "cars", "boats", "planes"];

  useEffect(() => {
    const fetchVehicles = async () => {
      try {
        setLoading(true);
        setError(null);
        const allVehicles: VehicleWithType[] = [];

        // Busca veículos de cada tipo
        for (const kind of vehicleKinds) {
          const response = await getAllVehicleByKind(kind, 0, 50);

          // Verifica se a resposta é um erro
          if (response && "statusCode" in response) {
            console.warn(`Erro ao buscar ${kind}:`, response);
            continue;
          }

          // Adiciona os veículos com seu tipo
          if (Array.isArray(response)) {
            const vehiclesWithType = response.map((vehicle: IVehicle) => ({
              ...vehicle,
              type: kind,
            }));
            allVehicles.push(...vehiclesWithType);
          }
        }

        setVehicles(allVehicles);
      } catch (err) {
        console.error("Erro ao buscar veículos:", err);
        setError("Erro ao carregar veículos. Tente novamente.");
      } finally {
        setLoading(false);
      }
    };

    fetchVehicles();
  }, []);

  const tabs = [
    { id: "bikes", label: "Bikes" },
    { id: "cars", label: "Cars" },
    { id: "boats", label: "Boats" },
    { id: "planes", label: "Planes" },
  ];

  const handleRentNow = (vehicle: VehicleWithType) => {
    // Salva os dados do veículo no sessionStorage
    sessionStorage.setItem(
      "purchasePageData",
      JSON.stringify({ vehicle: { type: vehicle.type, info: vehicle } }),
    );
    // Navega para a página de checkout de aluguel
    navigate("/rentingCheckoutPage");
  };

  const filteredVehicles =
    activeTab === "all"
      ? vehicles
      : vehicles.filter((v) => v.type === activeTab);

  return (
    <div className="bg-slate-50">
      <div className="container mx-auto px-4">
        {/* Error State */}
        {error && (
          <div className="mb-8 p-4 bg-red-50 border border-red-200 rounded-lg">
            <p className="text-red-700">{error}</p>
          </div>
        )}

        {/* Loading State */}
        {loading ? (
          <div className="flex justify-center items-center py-20">
            <LoadingSection />
          </div>
        ) : (
          <>
            <div className="mb-12 text-center">
              <h1 className="text-4xl font-bold text-slate-900 tracking-tight sm:text-5xl">
                Rent a Vehicle
              </h1>
              <p className="text-lg text-slate-600 mt-4 max-w-2xl mx-auto">
                Choose from our wide range of premium vehicles available for daily,
                weekly, or monthly rental. Find the perfect ride for your next
                adventure.
              </p>
            </div>
            {/* Filter Tabs */}
            <div className="flex flex-wrap justify-center gap-2 mb-10">
              {tabs.map((tab) => (
                <Button
                  key={tab.id}
                  variant={activeTab === tab.id ? "default" : "outline"}
                  onClick={() => setActiveTab(tab.id)}
                  className="min-w-[100px] rounded-full"
                >
                  {tab.label}
                </Button>
              ))}
            </div>

            {/* Vehicle Grid */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-8">
              {filteredVehicles.map((item) => (
                <Card
                  key={item.id}
                  className="group flex flex-col overflow-hidden border-slate-200 hover:shadow-xl transition-all duration-300 bg-white"
                >
                  <div className="aspect-[4/3] w-full overflow-hidden bg-slate-100 relative">
                    <picture className="w-full h-full block">
                      {item.webp && (
                        <source srcSet={item.webp} type="image/webp" />
                      )}
                      <img
                        src={item.images?.[0] || item.webp || ""}
                        alt={item.name}
                        className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
                      />
                    </picture>
                    <div className="absolute top-3 right-3 bg-white/90 backdrop-blur-sm px-2.5 py-1 rounded-md text-xs font-semibold text-slate-900 shadow-sm">
                      {item.category}
                    </div>
                  </div>
                  <CardHeader className="pb-2">
                    <div className="flex justify-between items-start gap-2">
                      <CardTitle className="text-xl font-bold line-clamp-1 text-slate-900">
                        {item.name}
                      </CardTitle>
                    </div>
                    <p className="text-lg font-bold ">
                      ${Number(item.price || 0).toFixed(2)} / day
                    </p>
                  </CardHeader>
                  <CardContent className="flex-grow">
                    <p className="text-sm text-slate-600 line-clamp-3">
                      {item.description}
                    </p>
                  </CardContent>
                  <CardFooter className="pt-0 mt-auto pb-6 px-6">
                    <Button
                      className="w-full"
                      size="lg"
                      onClick={() => handleRentNow(item)}
                    >
                      Rent Now
                    </Button>
                  </CardFooter>
                </Card>
              ))}
            </div>

            {!loading && filteredVehicles.length === 0 && (
              <div className="text-center py-20">
                <p className="text-slate-500 text-lg">
                  No vehicles found in this category.
                </p>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
};
