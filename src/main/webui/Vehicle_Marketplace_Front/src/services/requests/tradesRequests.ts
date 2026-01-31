import { PaymentInterface, RentingInterface } from "@/interfaces/tradeInterface";
import { execRequest } from "./genericRequests";

const { BACKEND_BASIC_URL } = process.env;

export const paymentRequest = async (data: PaymentInterface) => { return execRequest('POST', `${BACKEND_BASIC_URL}/payment`, data) }
export const rentingRequest = async (data: RentingInterface) => { return execRequest('POST', `${BACKEND_BASIC_URL}/payment/reting`, data) }