import {
  IPaymentInterface,
  IRentingInterface,
} from "@/interfaces/tradeInterface";
import { execRequest } from "./genericRequests";

const { VITE_BACKEND_BASIC_URL } = import.meta.env;

export const paymentRequest = async (data: IPaymentInterface) => {
  return execRequest("POST", `${VITE_BACKEND_BASIC_URL}/payment`, data);
};
export const rentingRequest = async (data: IRentingInterface) => {
  return execRequest("POST", `${VITE_BACKEND_BASIC_URL}/payment/reting`, data);
};
