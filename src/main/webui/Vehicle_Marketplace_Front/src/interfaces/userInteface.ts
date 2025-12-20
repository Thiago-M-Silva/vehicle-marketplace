import { EUserRoles } from "@/enums/ERoles";

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
}