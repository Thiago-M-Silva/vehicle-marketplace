export interface VehicleSearchInterface {
    model: string,
    yearMin: number,
    yearMax: number,
    priceMin: number,
    priceMax: number,
    category: string,
    color: string,
    sortBy: string,
    direction: string,
    page: number,
    size: number
    brand: string
}