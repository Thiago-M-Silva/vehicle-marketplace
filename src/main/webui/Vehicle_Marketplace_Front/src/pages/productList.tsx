import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardTitle, CardDescription, CardContent, CardFooter } from "@/components/ui/card";

type Props = {
    type: string;
}

export const ProductList = ({ type }: Props) => {
    const vehicle: any = async (type: string) => {
        
    };

    return ( 
        <div>
          <Card className="group flex flex-col items-center text-center hover:shadow-lg transition-shadow duration-300 border-slate-200">
            <CardHeader className="w-full">
              <CardTitle className="text-2xl">Announce Vehicle</CardTitle>
              <CardDescription>
                Ready to sell? Reach thousands of potential buyers.
              </CardDescription>
            </CardHeader>
            <CardContent className="w-full">
              <div className="aspect-video w-full overflow-hidden rounded-md mb-6">
                <picture className="w-full h-full block transition-all duration-300 group-hover:scale-105">
                  <source srcSet='' type="image/webp" />
                  <img
                    src=''
                    alt="Announce vehicle"
                    className="w-full h-full object-cover"
                  />
                </picture>
              </div>
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
        </div>
    );
}