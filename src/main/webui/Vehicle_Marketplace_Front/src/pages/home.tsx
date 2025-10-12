import { Button } from "@/components/ui/button"
import Header from "@/sections/header";

export const Home = () => {
    return (
        <>
            <Header />
            <div>HOME PAGE</div>
            <div className="flex min-h-svh flex-col items-center justify-center">
            <Button>Click me</Button>
            </div>
        </>
    )
}

export default Home;