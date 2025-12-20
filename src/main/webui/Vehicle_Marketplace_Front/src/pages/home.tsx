import Autoplay from "embla-carousel-autoplay";
import {
  Carousel,
  CarouselContent,
  CarouselItem
} from "@/components/ui/carousel";
import Header from "@/sections/header";
import bikeImage from "../assets/horse_power_vehicle_moto.png";
import boatImage from "../assets/horse_power_vehicle_boat.png";
import carImage from "../assets/horse_power_vehicle_car.png";
import planeImage from "../assets/horse_power_vehicle_aircraft.png";
import { VehicleResumeSection } from "@/sections/vehicleResumeSection";

export const Home = () => {
  return (
    <div className="border-2 border-amber-950">
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
    </div>
  );
};

export default Home;
