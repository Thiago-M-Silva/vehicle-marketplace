import { execRequest } from "./genericRequests";

const { VITE_BACKEND_VEHICLE_URL } = import.meta.env;

//TODO: finish this later
export const uploadImage = async (id: string, uploadImage: any) => {
  let data = new FormData();
  data.append("file", uploadImage);
  data.append("filename", uploadImage.name);
  data.append("contentType", uploadImage.type);

  return execRequest("POST", `${VITE_BACKEND_VEHICLE_URL}/${id}`, data);
};

export const downloadImage = async (id: string, filename: string) => {
  return execRequest("GET", `${VITE_BACKEND_VEHICLE_URL}/${id}/${filename}`, null);
};
