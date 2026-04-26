export interface IPaymentInterface {
    amount: number,
    currency: string,
    sellerAccountId: string,
    applicationFee: number,
    vehicleId: string,
    vehicleType: string,
    receiptEmail: string,
}

export interface IRentingInterface {
    vehicleId: string,
    vehicleType: string,
    customerId: string,
    sellerAccountId: string,
    applicationFee: number
}