import { CreateVehicleObject, CreateVehicleWithDocsObject } from '@/interfaces/createVehicleInterface';
import { VehicleSearchInterface } from '@/interfaces/vehicleSearchInterface';
import { execRequest } from './genericRequests';

const { BACKEND_VEHICLE_URL } = process.env;

export const getAllVehicleByKind = async (kind: string) => { return execRequest('GET', `${BACKEND_VEHICLE_URL}/get/${kind}`, null)}
export const getVehicleByKindAndId = async (kind: string, id: string) => { return execRequest('GET', `${BACKEND_VEHICLE_URL}/get/${kind}/${id}`, null)}

export const searchVehicles = async (kind: string, params: Partial<VehicleSearchInterface>) => {
    const url = new URL(`${BACKEND_VEHICLE_URL}/search/${kind}?`);
    for (const [key, value] of Object.entries(params)) {
        url.searchParams.append(key, String(value));
    }
    
    return execRequest('GET', url.toString(), null)
}

export const createManyVehicles = async (kind: string, vehicles: CreateVehicleObject[]) => { return execRequest('POST', `${BACKEND_VEHICLE_URL}/save/saveAllVehicles/${kind}`, vehicles) }
export const createOneVehicle = async (kind: string, vehicle: CreateVehicleObject) => { return execRequest('POST', `${BACKEND_VEHICLE_URL}/save/${kind}`, vehicle) }
export const createVehicleWithDocs = async (kind: string, vehicle: CreateVehicleWithDocsObject) => { return execRequest('POST', `${BACKEND_VEHICLE_URL}/save/${kind}/docs`, vehicle) }

export const deleteVehicleById = async (kind: string, id: string) => { return execRequest('DELETE', `${BACKEND_VEHICLE_URL}/delete/${kind}/${id}`, null) }
export const deleteManyVehicles = async (kind: string, ids: string[]) => { return execRequest('DELETE', `${BACKEND_VEHICLE_URL}/delete/${kind}`, ids) }

export const editVehicleById = async (kind: string, id: string, vehicle: CreateVehicleObject) => { return execRequest('PUT', `${BACKEND_VEHICLE_URL}/edit/${kind}/${id}`, vehicle) }
