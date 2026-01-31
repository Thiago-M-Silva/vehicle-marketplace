import { UUID } from "crypto";
import { IBike, IBoat, ICar, IPlane } from "./vehiclesInteface";
import { EUserRoles } from "@/enums/ERoles";

export interface User {
  id: UUID,
  name: string,
  email: string,
  password: string,
  phoneNumber: string,
  address: string,
  city: string,
  state: string,
  country: string,
  cpf: string,
  rg: string,
  stripeAccountId: string,
  birthDate: Date,
  bikes: IBike,
  boats: IBoat,
  cars: ICar,
  planes: IPlane,
  userRole: EUserRoles
}