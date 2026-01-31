import { EUserRoles } from "@/enums/ERoles";
import { IBike, IBoat, ICar, IPlane } from "./vehiclesInteface";

export interface IUser {
    id: string;
    address: string;
    birthDate: Date;
    email: string;
    city: string;
    country: string;
    cpf: string;
    name: string;
    password: string;
    phoneNumber: string;
    rg: string;
    state: string;
    userType: EUserRoles;
    Bike?: IBike;
    Boat?: IBoat;
    Car?: ICar;
    Plane?: IPlane;
}