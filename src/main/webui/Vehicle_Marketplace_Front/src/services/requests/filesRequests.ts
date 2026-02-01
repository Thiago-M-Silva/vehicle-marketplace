import { execRequest } from "./genericRequests";

const { BACKEND_VEHICLE_URL } = process.env;

//TODO: finish this later
export const uploadImage = async (id: string, uploadImage: any) => { 
    let data = new FormData();
    data.append("file", uploadImage);
    data.append("filename", uploadImage.name);
    data.append("contentType", uploadImage.type);
    
    return execRequest('POST', `${BACKEND_VEHICLE_URL}/${id}`, data) 
}

export const downloadImage = async (id: string, filename: string) => { 
    return execRequest('GET', `${BACKEND_VEHICLE_URL}/${id}/${filename}`, null) 
}