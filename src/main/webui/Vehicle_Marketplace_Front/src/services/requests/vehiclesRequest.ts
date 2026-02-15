import {
  CreateVehicleObject,
  CreateVehicleWithDocsObject,
} from "@/interfaces/createVehicleInterface";
import { VehicleSearchInterface } from "@/interfaces/vehicleSearchInterface";
import { execRequest } from "./genericRequests";
import { IVehicle } from "@/interfaces/vehiclesInteface";

const { VITE_BACKEND_VEHICLE_URL } = import.meta.env;

export const getAllVehicleByKind = async (kind: string) => {
  return execRequest("GET", `${VITE_BACKEND_VEHICLE_URL}/get/${kind}`, null);
};
export const getVehicleByKindAndId = async (kind: string, id: string) => {
  return execRequest("GET", `${VITE_BACKEND_VEHICLE_URL}/get/${kind}/${id}`, null);
};

export const searchVehicles = async (
  kind: string,
  params: Partial<VehicleSearchInterface>,
): Promise<IVehicle[]> => {
  const url = new URL(`${VITE_BACKEND_VEHICLE_URL}/search/${kind}?`);
  for (const [key, value] of Object.entries(params)) {
    url.searchParams.append(key, String(value));
  }

  const res: Promise<IVehicle[]> = await execRequest("GET", url.toString(), null);

  return res;
};

export const createManyVehicles = async (
  kind: string,
  vehicles: CreateVehicleObject[],
) => {
  return execRequest(
    "POST",
    `${VITE_BACKEND_VEHICLE_URL}/save/saveAllVehicles/${kind}`,
    vehicles,
  );
};
export const createOneVehicle = async (
  kind: string,
  vehicle: CreateVehicleObject,
) => {
  return execRequest("POST", `${VITE_BACKEND_VEHICLE_URL}/save/${kind}`, vehicle);
};
export const createVehicleWithDocs = async (
  kind: string,
  vehicle: CreateVehicleWithDocsObject,
) => {
  return execRequest(
    "POST",
    `${VITE_BACKEND_VEHICLE_URL}/save/${kind}/docs`,
    vehicle,
  );
};

export const deleteVehicleById = async (kind: string, id: string) => {
  return execRequest(
    "DELETE",
    `${VITE_BACKEND_VEHICLE_URL}/delete/${kind}/${id}`,
    null,
  );
};
export const deleteManyVehicles = async (kind: string, ids: string[]) => {
  return execRequest("DELETE", `${VITE_BACKEND_VEHICLE_URL}/delete/${kind}`, ids);
};

export const editVehicleById = async (
  kind: string,
  id: string,
  vehicle: CreateVehicleObject,
) => {
  return execRequest(
    "PUT",
    `${VITE_BACKEND_VEHICLE_URL}/edit/${kind}/${id}`,
    vehicle,
  );
};
