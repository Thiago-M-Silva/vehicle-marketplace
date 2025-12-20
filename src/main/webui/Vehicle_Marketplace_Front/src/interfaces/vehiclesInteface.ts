import { ECategory } from "@/enums/ECategory";
import { EColors } from "@/enums/EColors";
import { EFuelType } from "@/enums/EFuelType";
import { EVehicleStatus } from "@/enums/EVehicleStatus";
import { IUser } from "./userInteface";

export interface IVehicle {
    id: string;
    name: string;
    brand: string;
    year: number;
    model: string;
    horsepower: number;
    transmissionType: string;
    description: string;
    storage: number;
    category: ECategory;
    vehicleStatus: EVehicleStatus;
    color: EColors;
    fuelType: EFuelType;
    owner: IUser;
    price: number;
    rentalPriceMonthly: number;
}

export interface IBike {
    type: 'bike';
    info: IVehicle;
}

export interface IBoat {
    type: 'boat';
    info: IVehicle;
}

export interface ICar {
    type: 'car';
    info: IVehicle;
}

export interface IPlane {
    type: 'plane';
    info: IVehicle;
}