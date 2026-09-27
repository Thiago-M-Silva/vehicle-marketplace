import {
  IPaymentInterface,
  IRentingInterface,
} from "@/interfaces/tradeInterface";
import { execRequest } from "./genericRequests";
import { BACKEND_BASIC_URL } from "@/config/endpoints";

export const paymentRequest = async (data: IPaymentInterface) => {
  return execRequest("POST", `${BACKEND_BASIC_URL}/payment`, data);
};
export const rentingRequest = async (data: IRentingInterface) => {
  return execRequest("POST", `${BACKEND_BASIC_URL}/payment/reting`, data);
};
