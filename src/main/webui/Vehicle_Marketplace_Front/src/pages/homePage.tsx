import { VehicleResumeSection } from "@/sections/vehicleResumeSection";
import { CarouselSection } from "@/sections/carouselSection";
import { ServicesSection } from "@/sections/servicesSection";

export const Home = () => {
  return (
    <>
      <CarouselSection />
      <VehicleResumeSection />
      <ServicesSection />
    </>
  );
};

export default Home;
