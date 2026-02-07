import { IBike, IBoat, ICar, IPlane } from "@/interfaces/vehiclesInteface";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";

type Props = {
  data: IBike | ICar | IBoat | IPlane | null;
};

export const Checkout = ({ data }: Props) => {
  // Fallback values if data is null (for preview purposes or graceful degradation)
  const vehicleName = (data as any)?.name || "Vehicle Name";
  const vehiclePrice = (data as any)?.price || "$ 0.00";
  const vehicleImage = (data as any)?.webp || (data as any)?.image || "";
  const vehicleDescription =
    (data as any)?.description || "No description available.";

  return (
    <div className="min-h-screen bg-slate-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="container mx-auto max-w-6xl">
        <h1 className="text-3xl font-bold tracking-tight text-slate-900 mb-8">
          Checkout
        </h1>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Left Column: Details & Form */}
          <div className="lg:col-span-2 space-y-6">
            {/* Vehicle Review Card */}
            <Card className="shadow-sm border-slate-200">
              <CardHeader>
                <CardTitle>Review Item</CardTitle>
              </CardHeader>
              <CardContent className="flex flex-col sm:flex-row gap-6">
                <div className="w-full sm:w-40 aspect-[4/3] bg-slate-100 rounded-md overflow-hidden flex-shrink-0 border border-slate-200">
                  {vehicleImage ? (
                    <img
                      src={vehicleImage}
                      alt={vehicleName}
                      className="w-full h-full object-cover"
                    />
                  ) : (
                    <div className="w-full h-full flex items-center justify-center text-slate-400 text-xs">
                      No Image
                    </div>
                  )}
                </div>
                <div className="flex flex-col justify-center space-y-2">
                  <h3 className="text-xl font-semibold text-slate-900">
                    {vehicleName}
                  </h3>
                  <p className="text-sm text-slate-600 line-clamp-2">
                    {vehicleDescription}
                  </p>
                </div>
              </CardContent>
            </Card>

            {/* Customer Info Form */}
            <Card className="shadow-sm border-slate-200">
              <CardHeader>
                <CardTitle>Billing Information</CardTitle>
                <CardDescription>
                  Please enter your billing details.
                </CardDescription>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      First Name
                    </label>
                    <Input placeholder="John" />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Last Name
                    </label>
                    <Input placeholder="Doe" />
                  </div>
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium text-slate-900">
                    Email Address
                  </label>
                  <Input type="email" placeholder="john.doe@example.com" />
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium text-slate-900">
                    Address
                  </label>
                  <Input placeholder="1234 Main St" />
                </div>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      City
                    </label>
                    <Input placeholder="New York" />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      State
                    </label>
                    <Input placeholder="NY" />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Zip Code
                    </label>
                    <Input placeholder="10001" />
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>

          {/* Right Column: Summary & Action */}
          <div className="lg:col-span-1">
            <Card className="shadow-lg border-slate-200 sticky top-24">
              <CardHeader className="bg-slate-50/50 border-b border-slate-100 pb-4">
                <CardTitle>Order Summary</CardTitle>
              </CardHeader>
              <CardContent className="pt-6 space-y-4">
                <div className="flex justify-between text-sm">
                  <span className="text-slate-600">Subtotal</span>
                  <span className="font-medium text-slate-900">
                    {vehiclePrice}
                  </span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-slate-600">Processing Fee</span>
                  <span className="font-medium text-slate-900">$ 0.00</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-slate-600">Tax</span>
                  <span className="font-medium text-slate-900">$ 0.00</span>
                </div>
                <div className="border-t border-slate-100 pt-4 flex justify-between items-center">
                  <span className="font-bold text-lg text-slate-900">
                    Total
                  </span>
                  <span className="font-bold text-lg text-slate-900">
                    {vehiclePrice}
                  </span>
                </div>
              </CardContent>
              <CardFooter className="pt-2 pb-6">
                <Button className="w-full py-6 text-lg" size="lg">
                  Complete Purchase
                </Button>
              </CardFooter>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
};
