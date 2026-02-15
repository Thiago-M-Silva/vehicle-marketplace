import {
  PaymentInterface,
  RentingInterface,
} from "@/interfaces/tradeInterface";
import { execRequest } from "./genericRequests";

const { VITE_BACKEND_BASIC_URL } = import.meta.env;

export const paymentRequest = async (data: PaymentInterface) => {
  return execRequest("POST", `${VITE_BACKEND_BASIC_URL}/payment`, data);
};
export const rentingRequest = async (data: RentingInterface) => {
  return execRequest("POST", `${VITE_BACKEND_BASIC_URL}/payment/reting`, data);
};
