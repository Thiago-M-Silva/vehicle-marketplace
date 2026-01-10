import { Button } from "@/components/ui/button";
import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
  CardFooter,
} from "@/components/ui/card";

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
          <Card className="flex flex-col items-center text-center hover:shadow-lg transition-shadow duration-300 border-slate-200">
            <CardHeader>
              <CardTitle className="text-2xl">Announce Vehicle</CardTitle>
              <CardDescription>
                Ready to sell? Reach thousands of potential buyers.
              </CardDescription>
            </CardHeader>
            <CardContent>
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

          <Card className="flex flex-col items-center text-center hover:shadow-lg transition-shadow duration-300 border-slate-200">
            <CardHeader>
              <CardTitle className="text-2xl">Rent Vehicle</CardTitle>
              <CardDescription>
                Need a ride for a weekend or a special occasion?
              </CardDescription>
            </CardHeader>
            <CardContent>
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
