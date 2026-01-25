import { ECategory } from "@/enums/ECategory";
import { EColors } from "@/enums/EColors";
import { EFuelType } from "@/enums/EFuelType";
import { EVehicleStatus } from "@/enums/EVehicleStatus";

export interface CreateVehicleObject {
    name: string,
    brand: string,
    year: 2006,
    price: 2500.00,
    model: string,
    horsepower: 400,
    transmissionType: string,
    description: string,
    storage: 10,
    vehicleStatus: EVehicleStatus,
    category: ECategory,
    color: EColors,
    fuelType: EFuelType,
    docs?: any
}

export interface CreateVehicleWithDocsObject {
    vehicle: CreateVehicleObject,
    file: any,
    filename: string,
    contentType: string
}