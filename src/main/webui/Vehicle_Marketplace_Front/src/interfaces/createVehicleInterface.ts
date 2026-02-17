import { ECategory } from "@/enums/ECategory";
import { EColors } from "@/enums/EColors";
import { EFuelType } from "@/enums/EFuelType";
import { EVehicleStatus } from "@/enums/EVehicleStatus";

export interface ICreateVehicleObject {
    name: string,
    brand: string,
    year: number,
    price: number,
    model: string,
    horsepower: number,
    transmissionType: string,
    description: string,
    storage: number,
    vehicleStatus: EVehicleStatus,
    category: ECategory,
    color: EColors,
    fuelType: EFuelType,
    docs?: any
}

export interface ICreateVehicleWithDocsObject {
    vehicle: ICreateVehicleObject,
    file: any,
    filename: string,
    contentType: string
}