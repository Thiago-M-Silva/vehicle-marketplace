import React, { useState } from "react";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardHeader,
  CardTitle,
  CardContent,
  CardFooter,
} from "@/components/ui/card";

// Import assets
import bikePng from "../assets/bike/horse_power_vehicle_moto.png";
import bikeWebp from "../assets/bike/horse_power_vehicle_moto.webp";
import boatPng from "../assets/boat/horse_power_vehicle_boat.png";
import boatWebp from "../assets/boat/horse_power_vehicle_boat.webp";
import carPng from "../assets/car/horse_power_vehicle_car.png";
import carWebp from "../assets/car/horse_power_vehicle_car.webp";
import planePng from "../assets/plane/horse_power_vehicle_aircraft.png";
import planeWebp from "../assets/plane/horse_power_vehicle_aircraft.webp";

type Props = {};

// Mock data for rental vehicles
const MOCK_RENTALS = [
  {
    id: 1,
    name: "Yamaha MT-07",
    price: "$ 120.00 / day",
    description:
      "Perfect for city cruising and weekend getaways. Agile and powerful.",
    image: bikePng,
    webp: bikeWebp,
    category: "Motorcycle",
    type: "bikes",
  },
  {
    id: 2,
    name: "Speedster S",
    price: "$ 350.00 / day",
    description:
      "Experience luxury and speed with this high-performance coupe.",
    image: carPng,
    webp: carWebp,
    category: "Car",
    type: "cars",
  },
  {
    id: 3,
    name: "Ocean Pro",
    price: "$ 500.00 / day",
    description:
      "Enjoy the open water with friends and family in this spacious boat.",
    image: boatPng,
    webp: boatWebp,
    category: "Boat",
    type: "boats",
  },
  {
    id: 4,
    name: "Cirrus SR22",
    price: "$ 1,200.00 / day",
    description:
      "Fly in style with the world's best-selling general aviation aircraft.",
    image: planePng,
    webp: planeWebp,
    category: "Plane",
    type: "planes",
  },
  {
    id: 5,
    name: "Ducati Panigale",
    price: "$ 200.00 / day",
    description: "A superbike for those who crave adrenaline and speed.",
    image: bikePng,
    webp: bikeWebp,
    category: "Motorcycle",
    type: "bikes",
  },
];

export const RentingPage = ({}: Props) => {
  const [activeTab, setActiveTab] = useState("all");

  const filteredVehicles =
    activeTab === "all"
      ? MOCK_RENTALS
      : MOCK_RENTALS.filter((v) => v.type === activeTab);

  const tabs = [
    { id: "all", label: "All Rentals" },
    { id: "bikes", label: "Bikes" },
    { id: "cars", label: "Cars" },
    { id: "boats", label: "Boats" },
    { id: "planes", label: "Planes" },
  ];

  return (
    <div className="min-h-screen bg-slate-50 py-12">
      <div className="container mx-auto px-4">
        {/* Header Section */}
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
                  <source srcSet={item.webp} type="image/webp" />
                  <img
                    src={item.image}
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
                <p className="text-lg font-bold text-blue-600">{item.price}</p>
              </CardHeader>
              <CardContent className="flex-grow">
                <p className="text-sm text-slate-600 line-clamp-3">
                  {item.description}
                </p>
              </CardContent>
              <CardFooter className="pt-0 mt-auto pb-6 px-6">
                <Button className="w-full" size="lg">
                  Rent Now
                </Button>
              </CardFooter>
            </Card>
          ))}
        </div>

        {filteredVehicles.length === 0 && (
          <div className="text-center py-20">
            <p className="text-slate-500 text-lg">
              No vehicles found in this category.
            </p>
          </div>
        )}
      </div>
    </div>
  );
};
