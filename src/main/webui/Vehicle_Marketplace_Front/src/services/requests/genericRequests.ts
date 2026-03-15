import { IBackendErrorMessageInterface } from "@/interfaces/backendErrorMessageInterface";
import axios from "axios";

export const execRequest = async (
  method: string,
  url: string,
  data: any,
): Promise<any | IBackendErrorMessageInterface> => {
  try {
    const res = await axios.request({
      method: method,
      url: url,
      data: data,
    });

    return res.data;
  } catch (error: any) {
    console.log(error);
    if (error.response) {
      throw error.response.data as IBackendErrorMessageInterface;
    }
    throw error;
  }
};
