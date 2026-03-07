import {
  ICreateVehicleObject,
  ICreateVehicleWithDocsObject,
} from "@/interfaces/createVehicleInterface";
import { VehicleSearchInterface } from "@/interfaces/vehicleSearchInterface";
import { execRequest } from "./genericRequests";
import { IVehicle } from "@/interfaces/vehiclesInteface";
import { IBackendErrorMessageInterface } from "@/interfaces/backendErrorMessageInterface";

const { VITE_BACKEND_VEHICLE_URL } = import.meta.env;

export const getAllVehicleByKind = async (kind: string, page: number, size: number): Promise<any | IBackendErrorMessageInterface> => {
  return execRequest("GET", `${VITE_BACKEND_VEHICLE_URL}/get/${kind}/${page}/${size}`, null );
};

export const getVehicleByKindAndId = async (kind: string, id: string): Promise<IVehicle | IBackendErrorMessageInterface> => {
  return execRequest("GET", `${VITE_BACKEND_VEHICLE_URL}/get/${kind}/${id}`, null);
};

export const searchVehicles = async (kind: string, params: Partial<VehicleSearchInterface>): Promise<any | IBackendErrorMessageInterface> => {
  const url = new URL(`${VITE_BACKEND_VEHICLE_URL}/get/search/${kind}?`);

  for (const [key, value] of Object.entries(params)) {
    url.searchParams.append(key, String(value));
  }

  const res: Promise<IVehicle[]> = await execRequest("GET", url.toString(), null);

  return res;
};

export const createManyVehicles = async (kind: string, vehicles: ICreateVehicleObject[]): Promise<any | IBackendErrorMessageInterface> => {
  return execRequest(
    "POST",
    `${VITE_BACKEND_VEHICLE_URL}/save/saveAllVehicles/${kind}`,
    vehicles,
  );
};

export const createOneVehicle = async (kind: string, vehicle: ICreateVehicleObject): Promise<any | IBackendErrorMessageInterface> => {
  return execRequest("POST", `${VITE_BACKEND_VEHICLE_URL}/save/${kind}`, vehicle);
};

export const createVehicleWithDocs = async (kind: string, vehicle: ICreateVehicleWithDocsObject): Promise<any | IBackendErrorMessageInterface> => {
  return execRequest(
    "POST",
    `${VITE_BACKEND_VEHICLE_URL}/save/${kind}/docs`,
    vehicle,
  );
};

export const deleteVehicleById = async (kind: string, id: string): Promise<any | IBackendErrorMessageInterface> => {
  return execRequest(
    "DELETE",
    `${VITE_BACKEND_VEHICLE_URL}/delete/${kind}/${id}`,
    null,
  );
};

export const deleteManyVehicles = async (kind: string, ids: string[]): Promise<any | IBackendErrorMessageInterface> => {
  return execRequest("DELETE", `${VITE_BACKEND_VEHICLE_URL}/delete/${kind}`, ids);
};

export const editVehicleById = async (kind: string, id: string, vehicle: ICreateVehicleObject): Promise<any | IBackendErrorMessageInterface> => {
  return execRequest(
    "PUT",
    `${VITE_BACKEND_VEHICLE_URL}/edit/${kind}/${id}`,
    vehicle,
  );
};
