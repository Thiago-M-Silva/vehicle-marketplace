import { IUser } from "@/interfaces/userInteface";
import { IBike, ICar, IBoat, IPlane } from "@/interfaces/vehiclesInteface";
import { Checkout } from "@/sections/checkoutSection";

export const PurchasePage = () => {
  const purchaseData = sessionStorage.getItem("purchasePageData");
  const currentUser = sessionStorage.getItem("currentUser");
  
  if(!purchaseData) return null;
  if(!currentUser) return null;

  const {vehicle} = JSON.parse(purchaseData);
  const user = JSON.parse(currentUser);

  const tradeData: {
    user: IUser;
    vehicle: IBike | ICar | IBoat | IPlane;
  } = {user: {} as IUser, vehicle: {} as IBike | ICar | IBoat | IPlane};

  tradeData.user = user;
  tradeData.vehicle = vehicle;

  console.log('tradeData: ', tradeData);
  
  return <Checkout data={tradeData} />;
};
