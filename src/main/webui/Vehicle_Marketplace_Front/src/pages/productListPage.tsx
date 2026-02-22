import React, { useState, useMemo, useEffect } from "react";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardHeader,
  CardTitle,
  CardContent,
  CardFooter,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { FilterSection } from "@/sections/filterSection";
import { VehicleSearchInterface } from "@/interfaces/vehicleSearchInterface";
import { IVehicle } from "@/interfaces/vehiclesInteface";
import { IBackendErrorMessageInterface } from "@/interfaces/backendErrorMessageInterface";
import {
  getAllVehicleByKind,
  searchVehicles,
} from "@/services/requests/vehiclesRequest";
import { LoadingSection } from "@/sections/loadingSection";
import { useNavigate } from "react-router";
import { ErrorSection } from "@/sections/errorSection";

export const ProductList = () => {
  const navigate = useNavigate();

  const [vehicles, setVehicles] = useState<Partial<IVehicle>[]>();
  const [loading, setLoading] = useState(true);
  const [kind, setKind] = useState("bikes");
  const [filters, setFilters] = useState<Partial<VehicleSearchInterface>>({});
  const [error, setError] = useState<IBackendErrorMessageInterface | null>(
    null,
  );
  const [searchParam, setSearchParam] = useState("");

  useEffect(() => {
    const fetchVehicles = async () => {
      setLoading(true);
      setError(null);

      try {
        const fetchedVehicles = await getAllVehicleByKind(kind);
        setVehicles(fetchedVehicles.data);
        setLoading(false);
      } catch (err: any) {
        setLoading(false);
        setError(
          err.response?.data || { status: 500, message: "Unexpected Error" },
        );
      }
    };
    fetchVehicles();
  }, [kind]);

  const handleFilterChange = (newFilters: Partial<VehicleSearchInterface>) => {
    setFilters((prev) => ({ ...prev, ...newFilters }));
  };

  const handleSearch = async () => {
    setLoading(true);
    setError(null);
    try {
      const searchResult: IVehicle[] = await searchVehicles(kind, filters);
      setVehicles(searchResult);
    } catch (err: any) {
      setError(err.response?.data || { status: 500, message: "Search Failed" });
    } finally {
      setLoading(false);
    }
  };

  const redirectToDetailsPage = (vehicle: Partial<IVehicle>) => {
    navigate(`/productInfo/${kind}/${vehicle.id}`, { state: { vehicle } });
  };

  const filteredVehicles = useMemo(() => {
    if (!vehicles) return [];
    if (!searchParam) return vehicles;

    const lowerSearchParam = searchParam.toLowerCase();
    return vehicles.filter(
      (vehicle) =>
        vehicle.brand?.toLowerCase().includes(lowerSearchParam) ||
        vehicle.model?.toLowerCase().includes(lowerSearchParam) ||
        vehicle.description?.toLowerCase().includes(lowerSearchParam) ||
        vehicle.name?.toLowerCase().includes(lowerSearchParam),
    );
  }, [vehicles, searchParam]);

  const tabs = [
    { id: "all", label: "All Vehicles" },
    { id: "bikes", label: "Bikes" },
    { id: "cars", label: "Cars" },
    { id: "boats", label: "Boats" },
    { id: "planes", label: "Planes" },
  ];

  if (loading) {
    return <LoadingSection />;
  }

  if (error) {
    return <ErrorSection error={error} />;
  }

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="container mx-auto px-4">
        <div className="py-8">
          <div className="relative mb-8">
            <Input
              placeholder="Search by model or brand..."
              className="h-12 text-lg pl-4 pr-12"
              value={searchParam || ""}
              onChange={(e) => setSearchParam(e.target.value)}
            />
            <Button
              className="absolute right-2 top-1/2 -translate-y-1/2"
              size="icon"
              onClick={handleSearch}
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="24"
                height="24"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
              >
                <circle cx="11" cy="11" r="8" />
                <path d="m21 21-4.3-4.3" />
              </svg>
            </Button>
          </div>

          <div className="flex flex-wrap justify-center gap-2 mb-8">
            {tabs.map((tab) => (
              <Button
                key={tab.id}
                variant={filters.category === tab.id ? "default" : "outline"}
                onClick={() => {
                  handleFilterChange({ category: tab.id });
                  setKind(tab.id);
                }}
                className="min-w-[100px]"
              >
                {tab.label}
              </Button>
            ))}
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-4 gap-8 items-start pb-12">
          <div className="lg:col-span-1">
            <FilterSection
              filters={filters}
              onFilterChange={handleFilterChange}
              onSearch={handleSearch}
            />
          </div>

          <div className="lg:col-span-3 grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
            {filteredVehicles.length > 0 ? (
              filteredVehicles.map((item) => (
                <Card
                  key={item.id}
                  className="group flex flex-col overflow-hidden border-slate-200 hover:shadow-xl transition-all duration-300 bg-white"
                >
                  <div className="aspect-[4/3] w-full overflow-hidden bg-slate-100 relative">
                    <picture className="w-full h-full block">
                      <source srcSet={item.webp} type="image/webp" />
                      <img
                        src={item.image}
                        alt={item.name}
                        className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
                      />
                    </picture>
                    <div className="absolute top-2 right-2 bg-white/90 backdrop-blur-sm px-2 py-1 rounded text-xs font-semibold text-slate-900 shadow-sm">
                      {item.category}
                    </div>
                  </div>
                  <CardHeader className="pb-2">
                    <CardTitle className="text-xl line-clamp-1">
                      {item.name}
                    </CardTitle>
                    <p className="text-lg font-bold text-slate-900">
                      {item.price}
                    </p>
                  </CardHeader>
                  <CardContent className="flex-grow">
                    <p className="text-sm text-slate-600 line-clamp-2">
                      {item.description}
                    </p>
                  </CardContent>
                  <CardFooter className="pt-0 mt-auto">
                    <Button
                      className="w-full"
                      onClick={() => redirectToDetailsPage(item)}
                    >
                      View Details
                    </Button>
                  </CardFooter>
                </Card>
              ))
            ) : (
              <div className="text-center py-20 col-span-full">
                <p className="text-slate-500 text-lg">
                  No vehicles found matching your criteria.
                </p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};
