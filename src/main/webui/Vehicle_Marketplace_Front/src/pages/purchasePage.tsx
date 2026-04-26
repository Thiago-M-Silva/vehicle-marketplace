import { IUser } from "@/interfaces/userInteface";
import { IBike, ICar, IBoat, IPlane } from "@/interfaces/vehiclesInteface";
import { Checkout } from "@/sections/checkoutSection";
import { data, useLocation } from "react-router";

export const PurchasePage = () => {
  const location = useLocation();

  //TODO PASSAR ESSES DADOS POR SESSION STORAGE
  const vehicleInfo = location.state?.vehicle;
  const userInfo = location.state?.user;

  const tradeData: {
    user: IUser;
    vehicle: IBike | ICar | IBoat | IPlane;
  } = {user: {} as IUser, vehicle: {} as IBike | ICar | IBoat | IPlane};

  tradeData.user = userInfo;
  tradeData.vehicle = vehicleInfo;

  console.log('tradeData: ', tradeData);
  
  return <Checkout data={tradeData} />;
};
