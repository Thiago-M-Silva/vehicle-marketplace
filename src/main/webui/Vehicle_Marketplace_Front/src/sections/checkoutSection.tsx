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
import { IPaymentInterface } from "@/interfaces/tradeInterface";
import { useEffect, useState } from "react";
import { paymentRequest } from "@/services/requests/tradesRequests";
import { IUser } from "@/interfaces/userInteface";
import { getUserByKeycloakId } from "@/services/requests/usersRequests";

type Props = {
  data: {
    user: IUser;
    vehicle: IBike | ICar | IBoat | IPlane;
  };
};

export const Checkout = ({ data }: Props) => {
  const [loading, setLoading] = useState(false);
  const [sellButtonMsg, setSellButtonMsg] = useState(false);
  const [buyerLoading, setBuyerLoading] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalMessage, setModalMessage] = useState("");
  const [buyer, setBuyer] = useState<IUser>();
  const [buyerEmail, setBuyerEmail] = useState(data.user.email || "");
  const [firstName, setFirstName] = useState(data.user.name || "");
  const [address, setAddress] = useState(data.user.address || "");
  const [city, setCity] = useState(data.user.city || "");
  const [state, setState] = useState(data.user.state || "");

  console.log("checkout", data);

  const vehicle = data.vehicle.info;
  const vehicleType = data.vehicle.type;

  useEffect(() => {
    if (!data.user.keycloakId) {
      setBuyer(data.user);
      setBuyerEmail(data.user.email || "");
      setFirstName(data.user.name || "");
      setAddress(data.user.address || "");
      setCity(data.user.city || "");
      setState(data.user.state || "");
      return;
    }

    let isMounted = true;

    const fetchBuyer = async () => {
      try {
        setBuyerLoading(true);
        if (!data.user.keycloakId) return;
        const fetchedBuyer = await getUserByKeycloakId(data.user.keycloakId);

        if (isMounted) {
          setBuyer(fetchedBuyer);
          setBuyerEmail(fetchedBuyer.email || data.user.email || "");
          setFirstName(fetchedBuyer.firstName || data.user.name || "");
          setAddress(fetchedBuyer.address || data.user.address || "");
          setCity(fetchedBuyer.city || data.user.city || "");
          setState(fetchedBuyer.state || data.user.state || "");
        }
      } catch (error) {
        console.error("Error loading buyer:", error);

        if (isMounted) {
          setBuyer(data.user);
          setBuyerEmail(data.user.email || "");
          setFirstName(data.user.name || "");
          setAddress(data.user.address || "");
          setCity(data.user.city || "");
          setState(data.user.state || "");
        }
      } finally {
        if (isMounted) {
          setBuyerLoading(false);
        }
      }
    };

    fetchBuyer();

    return () => {
      isMounted = false;
    };
  }, [data.user]);

  const handlePurchase = async () => {
    const tradeData: IPaymentInterface = {
      amount: vehicle.price,
      currency: "USD",
      sellerAccountId: vehicle.owner?.stripeAccountId || "",
      applicationFee: 0,
      vehicleId: vehicle.id,
      vehicleType: vehicleType,
      receiptEmail: buyerEmail || buyer?.email || data.user.email || "",
    };

    try {
      setLoading(true);
      await paymentRequest(tradeData);
    } catch (error) {
      console.error("Error processing purchase:", error);
      setModalMessage("Compra não permitida na demo");
      setIsModalOpen(true);
    } finally {
      setLoading(false);
    }
  };

  // Fallback values if data is null (for preview purposes or graceful degradation)
  const vehicleName = vehicle.name || "Vehicle Name";
  const vehiclePriceFormatted = new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD",
  }).format(vehicle.price || 0);
  const vehicleImage = vehicle.webp || vehicle.images?.[0] || "";
  const vehicleDescription = vehicle.description || "No description available.";

  const handleButtonText = () => setSellButtonMsg(!sellButtonMsg);

  return (
    <div className="min-h-screen bg-slate-50 py-12 px-4 sm:px-6 lg:px-8">
      {isModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 px-4">
          <div className="w-full max-w-md rounded-lg bg-white p-6 shadow-xl">
            <h2 className="text-lg font-semibold text-slate-900">Atenção</h2>
            <p className="mt-2 text-sm text-slate-600">{modalMessage}</p>
            <div className="mt-6 flex justify-end">
              <Button onClick={() => setIsModalOpen(false)}>Fechar</Button>
            </div>
          </div>
        </div>
      )}
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
                    <Input
                      placeholder="John"
                      value={firstName}
                      onChange={(event) => setFirstName(event.target.value)}
                    />
                  </div>
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium text-slate-900">
                    Email Address
                  </label>
                  <Input
                    type="email"
                    placeholder="john@example.com"
                    value={buyerEmail}
                    onChange={(event) => setBuyerEmail(event.target.value)}
                  />
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium text-slate-900">
                    Address
                  </label>
                  <Input
                    placeholder="1234 Main St"
                    value={address}
                    onChange={(event) => setAddress(event.target.value)}
                  />
                </div>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      City
                    </label>
                    <Input
                      placeholder="New York"
                      value={city}
                      onChange={(event) => setCity(event.target.value)}
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      State
                    </label>
                    <Input
                      placeholder="NY"
                      value={state}
                      onChange={(event) => setState(event.target.value)}
                    />
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
                    {vehiclePriceFormatted}
                  </span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-slate-600">Processing Fee</span>
                  <span className="font-medium text-slate-900">$0.00</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-slate-600">Tax</span>
                  <span className="font-medium text-slate-900">$0.00</span>
                </div>
                <div className="border-t border-slate-100 pt-4 flex justify-between items-center">
                  <span className="font-bold text-lg text-slate-900">
                    Total
                  </span>
                  <span className="font-bold text-lg text-slate-900">
                    {vehiclePriceFormatted}
                  </span>
                </div>
              </CardContent>
              <CardFooter className="pt-2 pb-6">
                {String(import.meta.env.VITE_ENV) === "DEV" ? (
                  <Button
                    className="w-full py-6 text-lg"
                    size="lg"
                    onClick={handlePurchase}
                    disabled={loading || buyerLoading}
                  >
                    {loading ? "Processing..." : "Complete Purchase"}
                  </Button>
                ) : (
                  <Button
                    className="w-full py-6 text-lg"
                    size="lg"
                    onClick={handleButtonText}
                    disabled={loading || buyerLoading}
                  >
                    {sellButtonMsg ? "Purchase available" : "Complete Purchase"}
                  </Button>
                )}
              </CardFooter>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
};
