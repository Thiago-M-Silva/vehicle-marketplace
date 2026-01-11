import Header from "@/sections/headerSection";
import { VehicleResumeSection } from "@/sections/vehicleResumeSection";
import { Footer } from "@/sections/footerSection";
import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";
import { SidebarSection } from "@/sections/sidebarSection";
import { CarouselSection } from "@/sections/carouselSection";
import { ServicesSection } from "@/sections/servicesSection";

export const Home = () => {
  return (
    <SidebarProvider>
      <SidebarSection />
      <main className="w-full">
        <SidebarTrigger className="md:hidden" />
        <Header />
        <CarouselSection />
        <VehicleResumeSection />
        <ServicesSection />
        <Footer />
      </main>
    </SidebarProvider>
  );
};

export default Home;
