import { IUser } from "@/interfaces/userInteface";
import { execRequest } from "./genericRequests";

const { BACKEND_USERS_URL } = process.env;

export const getAllUsers = async () => { return execRequest('GET', `${BACKEND_USERS_URL}/get`, null) }
export const getUserById = async (id: string) => { return execRequest('GET', `${BACKEND_USERS_URL}/get/${id}`, null) }

export const createUser = async (data: Partial<IUser>) => { return execRequest('POST', `${BACKEND_USERS_URL}/save`, data) }

export const editUserById = async (id: string, data: IUser) => { return execRequest('PUT', `${BACKEND_USERS_URL}/put/${id}`, data) }
export const createStripeConnectAccount = async (id: string) => { return execRequest('PUT', `${BACKEND_USERS_URL}/put/setSeller/${id}`, null) }

export const deleteUserById = async (id: string) => { return execRequest('DELETE', `${BACKEND_USERS_URL}/delete/${id}`, null) }