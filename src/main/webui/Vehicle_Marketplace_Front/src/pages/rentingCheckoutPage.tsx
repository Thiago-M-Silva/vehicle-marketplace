import { useEffect, useState } from "react";
import { IBike, IBoat, ICar, IPlane } from "@/interfaces/vehiclesInteface";
import { IUser } from "@/interfaces/userInteface";
import { Checkout } from "@/sections/rentCheckoutSection";
import { useNavigate } from "react-router";

type TypedVehicle = IBike | ICar | IBoat | IPlane;

export const RentingCheckoutPage = () => {
  const navigate = useNavigate();
  const [vehicle, setVehicle] = useState<TypedVehicle | null>(null);
  const [user, setUser] = useState<IUser | undefined>(undefined);

  useEffect(() => {
    // Resgata os dados do veículo do sessionStorage
    const purchasePageData = sessionStorage.getItem("purchasePageData");
    if (purchasePageData) {
      try {
        const { vehicle } = JSON.parse(purchasePageData);
        setVehicle(vehicle);
      } catch (error) {
        console.error("Erro ao resgatar dados do veículo:", error);
        navigate("/rentingPage");
      }
    } else {
      navigate("/rentingPage");
    }

    // Resgata os dados do usuário do sessionStorage
    const currentUser = sessionStorage.getItem("currentUser");
    if (currentUser) {
      try {
        setUser(JSON.parse(currentUser));
      } catch (error) {
        console.error("Erro ao resgatar dados do usuário:", error);
      }
    }
  }, [navigate]);

  if (!vehicle) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <p className="text-lg text-slate-600">Carregando...</p>
      </div>
    );
  }

  return <Checkout data={vehicle} user={user} />;
};
