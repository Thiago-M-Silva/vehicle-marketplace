import { execRequest } from "./requests/genericRequests";
import { form-data } from "form-data";

const { BACKEND_VEHICLE_URL } = process.env;



export const uploadImage = async (id: string, uploadImage: any) => { 
    let data = new FormData();
    data.append("file", uploadImage);
    data.append("filename", uploadImage.name);
    data.append("contentType", uploadImage.type);
    
    return execRequest('POST', `${BACKEND_VEHICLE_URL}/${id}`, data) 
}