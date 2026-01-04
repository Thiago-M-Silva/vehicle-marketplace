import Autoplay from "embla-carousel-autoplay";
import {
  Carousel,
  CarouselContent,
  CarouselItem,
} from "@/components/ui/carousel";
import bikePng from "../assets/bike/horse_power_vehicle_moto.png";
import bikeWebp from "../assets/bike/horse_power_vehicle_moto.webp";
import boatPng from "../assets/boat/horse_power_vehicle_boat.png";
import boatWebp from "../assets/boat/horse_power_vehicle_boat.webp";
import carPng from "../assets/car/horse_power_vehicle_car.png";
import carWebp from "../assets/car/horse_power_vehicle_car.webp";
import planePng from "../assets/plane/horse_power_vehicle_aircraft.png";
import planeWebp from "../assets/plane/horse_power_vehicle_aircraft.webp";

export const CarouselSection = () => {
  const items = [
    {
      webp: bikeWebp,
      png: bikePng,
      alt: "A modern sport motorcycle",
    },
    {
      webp: boatWebp,
      png: boatPng,
      alt: "A speedboat on the water",
    },
    {
      webp: carWebp,
      png: carPng,
      alt: "A luxury sports car",
    },
    {
      webp: planeWebp,
      png: planePng,
      alt: "A private jet on the runway",
    },
  ];

  return (
    <Carousel
      opts={{
        loop: true,
      }}
      plugins={[
        Autoplay({
          delay: 3000
        }),
      ]}
    >
      <CarouselContent>
        {items.map((item, index) => (
          <CarouselItem key={index} className="h-[80vh] group relative">
            <picture className="w-full h-full block transition-all duration-300 group-hover:blur-sm">
              <source srcSet={item.webp} type="image/webp" />
              <img
                src={item.png}
                alt={item.alt}
                className="w-full h-full object-cover"
              />
            </picture>
            <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity duration-300">
              <a
                href="/product"
                className="bg-white text-black px-6 py-2 rounded-full font-semibold shadow-lg hover:bg-gray-100 transition-colors"
              >
                See more
              </a>
            </div>
          </CarouselItem>
        ))}
      </CarouselContent>
    </Carousel>
  );
};
