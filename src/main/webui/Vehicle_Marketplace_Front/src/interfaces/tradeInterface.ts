export interface PaymentInterface {
    amount: number,
    currency: string,
    sellerAccountId: string,
    applicationFee: number,
    vehicleId: string,
    vehicleType: string,
    receiptEmail: string,
}

export interface RentingInterface {
    vehicleId: string,
    vehicleType: string,
    customerId: string,
    sellerAccountId: string,
    applicationFee: number
}