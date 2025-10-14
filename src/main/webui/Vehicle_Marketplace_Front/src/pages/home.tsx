import Autoplay from "embla-carousel-autoplay"
import { Button } from "@/components/ui/button"
import { Carousel, CarouselContent, CarouselItem, CarouselPrevious, CarouselNext } from "@/components/ui/carousel";
import Header from "@/sections/header";

export const Home = () => {
    return (
        <>
            <Header />
            <div>HOME PAGE</div>
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
            <div className="flex min-h-svh flex-col items-center justify-center">
                <Button>Click me</Button>
            </div>
        </>
    )
}

export default Home;