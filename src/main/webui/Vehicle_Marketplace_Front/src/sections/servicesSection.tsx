import { Button } from "@/components/ui/button";
import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
  CardFooter,
} from "@/components/ui/card";

import rentWebp from "../assets/others/rent/renting_image.webp";
import rentPng from "../assets/others/rent/renting_image.png";
import announceWebp from "../assets/others/announce/announce_image.webp";
import announcePng from "../assets/others/announce/announce_image.png";

export const ServicesSection = () => {
  return (
    <section className="w-full py-16 bg-white">
      <div className="container mx-auto px-4">
        <div className="text-center mb-12">
          <h2 className="text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">
            Our Services
          </h2>
          <p className="mt-4 text-lg text-gray-600">
            Beyond buying, we offer comprehensive solutions for your vehicle
            needs.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-8 max-w-4xl mx-auto">
          <Card className="group flex flex-col items-center text-center hover:shadow-lg transition-shadow duration-300 border-slate-200">
            <CardHeader className="w-full">
              <CardTitle className="text-2xl">Announce Vehicle</CardTitle>
              <CardDescription>
                Ready to sell? Reach thousands of potential buyers.
              </CardDescription>
            </CardHeader>
            <CardContent className="w-full">
              <div className="aspect-video w-full overflow-hidden rounded-md mb-6">
                <picture className="w-full h-full block transition-all duration-300 group-hover:scale-105">
                  <source srcSet={announceWebp} type="image/webp" />
                  <img
                    src={announcePng}
                    alt="Announce vehicle"
                    className="w-full h-full object-cover"
                  />
                </picture>
              </div>
              <p className="text-gray-600">
                Create a listing in minutes and get the best value for your
                bike, car, boat, or plane.
              </p>
            </CardContent>
            <CardFooter className="mt-auto pb-8">
              <Button size="lg" className="w-full sm:w-auto">
                Start Selling
              </Button>
            </CardFooter>
          </Card>

          <Card className="group flex flex-col items-center text-center hover:shadow-lg transition-shadow duration-300 border-slate-200">
            <CardHeader className="w-full">
              <CardTitle className="text-2xl">Rent Vehicle</CardTitle>
              <CardDescription>
                Need a ride for a weekend or a special occasion?
              </CardDescription>
            </CardHeader>
            <CardContent className="w-full">
              <div className="aspect-video w-full overflow-hidden rounded-md mb-6">
                <picture className="w-full h-full block transition-all duration-300 group-hover:scale-105">
                  <source srcSet={rentWebp} type="image/webp" />
                  <img
                    src={rentPng}
                    alt="Renting vehicle"
                    className="w-full h-full object-cover"
                  />
                </picture>
              </div>
              <p className="text-gray-600">
                Explore our premium fleet available for short-term and long-term
                rentals.
              </p>
            </CardContent>
            <CardFooter className="mt-auto pb-8">
              <Button size="lg" className="w-full sm:w-auto">
                Find a Rental
              </Button>
            </CardFooter>
          </Card>
        </div>
      </div>
    </section>
  );
};
