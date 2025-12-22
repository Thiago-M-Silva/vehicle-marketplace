import Autoplay from "embla-carousel-autoplay";
import {
  Carousel,
  CarouselContent,
  CarouselItem,
} from "@/components/ui/carousel";
import Header from "@/sections/header";
import bikeImage from "../assets/horse_power_vehicle_moto.png";
import boatImage from "../assets/horse_power_vehicle_boat.png";
import carImage from "../assets/horse_power_vehicle_car.png";
import planeImage from "../assets/horse_power_vehicle_aircraft.png";
import { VehicleResumeSection } from "@/sections/vehicleResumeSection";
import { Footer } from "@/sections/footer";
import { Button } from "@/components/ui/button";
import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";
import { SidebarSection } from "@/sections/sidebar";

export const Home = () => {
  return (
    <SidebarProvider>
      <SidebarSection />
      <main className="w-full">
        <SidebarTrigger />
        <Header />
        <Carousel
          opts={{
            loop: true,
          }}
          plugins={[
            Autoplay({
              delay: 3000,
            }),
          ]}
        >
          <CarouselContent>
            <CarouselItem className="h-[80vh]">
              <img
                src={bikeImage}
                alt="A modern sport motorcycle"
                className="w-full h-full object-cover"
              />
            </CarouselItem>
            <CarouselItem className="h-[80vh]">
              <img
                src={boatImage}
                alt="A speedboat on the water"
                className="w-full h-full object-cover"
              />
            </CarouselItem>
            <CarouselItem className="h-[80vh]">
              <img
                src={carImage}
                alt="A luxury sports car"
                className="w-full h-full object-cover"
              />
            </CarouselItem>
            <CarouselItem className="h-[80vh]">
              <img
                src={planeImage}
                alt="A private jet on the runway"
                className="w-full h-full object-cover"
              />
            </CarouselItem>
          </CarouselContent>
        </Carousel>
        <VehicleResumeSection />
        <div className="w-full my-16 h-24">
          <h2 className="text-center text-2xl my-4">Other services</h2>
          <div className="flex justify-center gap-2">
            <div className="mx-10">
              <h3>Announce vehicle</h3>
              <Button>See more</Button>
            </div>
            <div className="mx-10">
              <h3>Rent vehicle</h3>
              <Button>See more</Button>
            </div>
          </div>
        </div>
        <Footer />
      </main>
    </SidebarProvider>
  );
};

export default Home;
