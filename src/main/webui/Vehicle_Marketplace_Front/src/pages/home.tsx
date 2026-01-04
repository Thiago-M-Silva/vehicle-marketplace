import Header from "@/sections/header";
import { VehicleResumeSection } from "@/sections/vehicleResumeSection";
import { Footer } from "@/sections/footer";
import { Button } from "@/components/ui/button";
import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";
import { SidebarSection } from "@/sections/sidebar";
import { CarouselSection } from "@/sections/carouselSection";

export const Home = () => {
  return (
    <SidebarProvider>
      <SidebarSection />
      <main className="w-full">
        <SidebarTrigger className="md:hidden" />
        <Header />
        <CarouselSection />
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
