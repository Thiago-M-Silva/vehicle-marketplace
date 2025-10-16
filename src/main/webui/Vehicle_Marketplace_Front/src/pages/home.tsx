import Autoplay from "embla-carousel-autoplay"
import { Carousel, CarouselContent, CarouselItem, CarouselPrevious, CarouselNext } from "@/components/ui/carousel";
import Header from "@/sections/header";

export const Home = () => {
    return (
        <div className="h-full w-full border-2 border-amber-950">
            <Header />
            <Carousel
                plugins={[
                    Autoplay({
                        delay: 2000,
                    }),
                ]}
            >
                <CarouselContent>
                    <CarouselItem>...</CarouselItem>
                    <CarouselItem>...</CarouselItem>
                    <CarouselItem>...</CarouselItem>
                </CarouselContent>
                <CarouselPrevious />
                <CarouselNext />
            </Carousel>
        </div>
    )
}

export default Home;