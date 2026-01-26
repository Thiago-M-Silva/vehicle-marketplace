import React from 'react'
import {
  IBike,
  ICar,
  IBoat,
  IPlane
} from '@/interfaces/vehiclesInteface'
import bikePng from "../assets/bike/horse_power_vehicle_moto.png";
import bikeWebp from "../assets/bike/horse_power_vehicle_moto.webp";
import { Table, TableRow, TableBody, TableCell } from '@/components/ui/table';
import { VehicleResumeSection } from '@/sections/vehicleResumeSection';
import { Button } from '@/components/ui/button';

type VehicleProp = {
  vehicle: IBike | ICar | IBoat | IPlane | null
}

export const ProductInfo = ({ vehicle }: VehicleProp) => {
  const item = {
    webp: bikeWebp,
    png: bikePng,
    alt: "A modern sport motorcycle",
    name: "Yamaha MT-07",
    price: "$ 8,999.00",
    description: "The MT-07 is designed to bring fun, affordability and enjoyment back to the street. Everything about this versatile naked bike – from its deep torque to the agile chassis and outstanding economy – make it irresistible to both newer and experienced riders."
  }

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="container mx-auto px-4 py-12">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-12">
          {/* Image Section */}
          <div className="w-full">
            <div className="aspect-[4/3] w-full overflow-hidden rounded-xl border border-slate-200 bg-white shadow-sm">
              <picture className="h-full w-full block">
                <source srcSet={item.webp} type="image/webp" />
                <img
                  src={item.png}
                  alt={item.alt}
                  className="h-full w-full object-cover transition-transform duration-500 hover:scale-105"
                />
              </picture>
            </div>
          </div>

          {/* Info Section */}
          <div className="flex flex-col gap-8">
            <div>
              <h1 className="text-3xl font-bold tracking-tight text-slate-900 sm:text-4xl">{item.name}</h1>
              <p className="mt-2 text-2xl font-semibold text-slate-900">{item.price}</p>
            </div>

            <div className="flex flex-col sm:flex-row gap-4">
              <Button size="lg" className="w-full sm:w-auto px-8">Purchase Vehicle</Button>
              <Button variant="outline" size="lg" className="w-full sm:w-auto px-8">Contact Seller</Button>
            </div>

            <div className="space-y-4">
              <h3 className="text-lg font-semibold text-slate-900">Technical Specifications</h3>
              <div className="rounded-lg border border-slate-200 bg-white overflow-hidden">
                <Table>
                  <TableBody>
                    <TableRow><TableCell className="font-medium text-slate-500 w-1/3">Brand</TableCell><TableCell>Yamaha</TableCell></TableRow>
                    <TableRow><TableCell className="font-medium text-slate-500">Model</TableCell><TableCell>MT-07</TableCell></TableRow>
                    <TableRow><TableCell className="font-medium text-slate-500">Year</TableCell><TableCell>2024</TableCell></TableRow>
                    <TableRow><TableCell className="font-medium text-slate-500">Category</TableCell><TableCell>Naked Bike</TableCell></TableRow>
                    <TableRow><TableCell className="font-medium text-slate-500">Fuel Type</TableCell><TableCell>Gasoline</TableCell></TableRow>
                    <TableRow><TableCell className="font-medium text-slate-500">Color</TableCell><TableCell>Ice Fluo</TableCell></TableRow>
                  </TableBody>
                </Table>
              </div>
            </div>

            <div className="space-y-4">
              <h3 className="text-lg font-semibold text-slate-900">Description</h3>
              <p className="text-slate-600 leading-relaxed">{item.description}</p>
            </div>
          </div>
        </div>
      </div>

      <div className="border-t border-slate-200 bg-white">
        <VehicleResumeSection />
      </div>
    </div>
  )
}
