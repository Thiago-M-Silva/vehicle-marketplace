import React, { useState } from "react";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardHeader,
  CardTitle,
  CardContent,
  CardFooter,
} from "@/components/ui/card";
import bikePng from "../assets/bike/horse_power_vehicle_moto.png";
import bikeWebp from "../assets/bike/horse_power_vehicle_moto.webp";

type Props = {
  type?: string;
  vehicle?: any;
};

// Mock data for display purposes
const MOCK_VEHICLES = [
  {
    id: 1,
    name: "Yamaha MT-07",
    price: "$ 8,999.00",
    description:
      "The MT-07 is designed to bring fun, affordability and enjoyment back to the street.",
    image: bikePng,
    webp: bikeWebp,
    category: "Motorcycle",
    type: "bikes",
  },
  {
    id: 2,
    name: "Speedster S",
    price: "$ 45,000.00",
    description: "High performance coupe with aerodynamic design.",
    image: bikePng, // Placeholder
    webp: bikeWebp,
    category: "Car",
    type: "cars",
  },
  {
    id: 3,
    name: "Ocean Pro",
    price: "$ 25,000.00",
    description: "Perfect for weekend getaways on the water.",
    image: bikePng, // Placeholder
    webp: bikeWebp,
    category: "Boat",
    type: "boats",
  },
  {
    id: 4,
    name: "Cirrus SR22",
    price: "$ 750,000.00",
    description: "The world's best-selling general aviation aircraft.",
    image: bikePng, // Placeholder
    webp: bikeWebp,
    category: "Plane",
    type: "planes",
  },
];

export const ProductList = ({ type }: Props) => {
  const [activeTab, setActiveTab] = useState(type || "all");

  const filteredVehicles =
    activeTab === "all"
      ? MOCK_VEHICLES
      : MOCK_VEHICLES.filter((v) => v.type === activeTab);

  const tabs = [
    { id: "all", label: "All Vehicles" },
    { id: "bikes", label: "Bikes" },
    { id: "cars", label: "Cars" },
    { id: "boats", label: "Boats" },
    { id: "planes", label: "Planes" },
  ];

  return (
    <div className="min-h-screen bg-slate-50 py-12">
      <div className="container mx-auto px-4">
        <div className="mb-8 text-center">
          <h1 className="text-3xl font-bold text-slate-900">
            Featured Vehicles
          </h1>
          <p className="text-slate-600 mt-2">
            Explore our selection of premium vehicles.
          </p>
        </div>

        <div className="flex flex-wrap justify-center gap-2 mb-8">
          {tabs.map((tab) => (
            <Button
              key={tab.id}
              variant={activeTab === tab.id ? "default" : "outline"}
              onClick={() => setActiveTab(tab.id)}
              className="min-w-[100px]"
            >
              {tab.label}
            </Button>
          ))}
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {filteredVehicles.map((item) => (
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
                <p className="text-lg font-bold text-slate-900">{item.price}</p>
              </CardHeader>
              <CardContent className="flex-grow">
                <p className="text-sm text-slate-600 line-clamp-2">
                  {item.description}
                </p>
              </CardContent>
              <CardFooter className="pt-0 mt-auto">
                <Button className="w-full">View Details</Button>
              </CardFooter>
            </Card>
          ))}
        </div>
      </div>
    </div>
  );
};
