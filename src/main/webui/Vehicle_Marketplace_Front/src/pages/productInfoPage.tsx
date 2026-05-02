import { useEffect, useState } from 'react'
import { IBike, IBoat, ICar, IPlane, IVehicle } from '@/interfaces/vehiclesInteface';
import { Table, TableRow, TableBody, TableCell } from '@/components/ui/table';
import { VehicleResumeSection } from '@/sections/vehicleResumeSection';
import { Button } from '@/components/ui/button';
import { useLocation, useParams } from 'react-router';
import { getVehicleByKindAndId } from '@/services/requests/vehiclesRequest';
import { useNavigate } from "react-router";

type TypedVehicle = IBike | ICar | IBoat | IPlane;

const vehicleTypeByKind: Record<string, TypedVehicle["type"]> = {
  bikes: "bike",
  cars: "car",
  boats: "boat",
  planes: "plane",
};

const toTypedVehicle = (vehicle: IVehicle, kind?: string): TypedVehicle | null => {
  console.log('Prod info vehicle', vehicle);
  
  const type = kind ? vehicleTypeByKind[kind] : undefined;

  return type ? { type, info: vehicle } as TypedVehicle : null;
};

export const ProductInfo = () => {
  const navigate = useNavigate();  
  const location = useLocation();
  const { kind, id } = useParams();
  const [vehicle, setVehicle] = useState<TypedVehicle | null>(() => {
    const stateVehicle = location.state?.vehicle;

    if (!stateVehicle) {
      return null;
    }

    return "info" in stateVehicle
      ? stateVehicle
      : toTypedVehicle(stateVehicle as IVehicle, kind);
  });

  useEffect(() => {
    if(!vehicle && id){
      const fetchVehicle = async () => {
        const response = await getVehicleByKindAndId(kind as string, id);
        setVehicle(toTypedVehicle(response as IVehicle, kind));
      }

      fetchVehicle();
    }
  }, [id, kind, vehicle])

  
  const redirectToCheckout = (byuOrRent: string) => {
    const currentUser = sessionStorage.getItem("currentUser");
    const user = currentUser ? JSON.parse(currentUser) : null;
    
    console.log('productInfo', vehicle, byuOrRent, user)
    window.sessionStorage.setItem('purchasePageData', JSON.stringify({vehicle}));
    byuOrRent === 'buy' ?  navigate('/purchasePage') : navigate('/rentingPage');
  }

  const item = vehicle?.info;

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="container mx-auto px-4 py-12">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-12">
          {/* Image Section */}
          <div className="w-full">
            <div className="aspect-[4/3] w-full overflow-hidden rounded-xl border border-slate-200 bg-white shadow-sm">
              <picture className="h-full w-full block">
                <source srcSet={item?.webp} type="image/webp" />
                  <img
                  src={item?.images?.[0]}
                  alt={item?.name}
                  className="h-full w-full object-cover transition-transform duration-500 hover:scale-105"
                />
              </picture>
            </div>
          </div>

          {/* Info Section */}
          <div className="flex flex-col gap-8">
            <div>
              <h1 className="text-3xl font-bold tracking-tight text-slate-900 sm:text-4xl">{item?.name}</h1>
              <p className="mt-2 text-2xl font-semibold text-slate-900">{item?.price}</p>
            </div>

            <div className="flex m-auto flex-col sm:flex-row gap-4">
              <Button size="lg" className="w-full sm:w-auto px-8" onClick={() => redirectToCheckout('buy')}>Purchase Vehicle</Button>
              <Button variant="outline" size="lg" className="w-full sm:w-auto px-8" onClick={() => redirectToCheckout('rent')}>Rent Vehicle</Button>
            </div>

            <div className="space-y-4">
              <h3 className="text-lg font-semibold text-slate-900">Technical Specifications</h3>
              <div className="rounded-lg border border-slate-200 bg-white overflow-hidden">
                <Table>
                  <TableBody>
                    <TableRow>
                      <TableCell className="font-medium text-slate-500 w-1/3">Brand</TableCell>
                      <TableCell>{item?.brand}</TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell className="font-medium text-slate-500">Model</TableCell>
                      <TableCell>{item?.model}</TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell className="font-medium text-slate-500">Year</TableCell>
                      <TableCell>{item?.year}</TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell className="font-medium text-slate-500">Category</TableCell>
                      <TableCell>{item?.category}</TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell className="font-medium text-slate-500">Fuel Type</TableCell>
                      <TableCell>{item?.fuelType}</TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell className="font-medium text-slate-500">Color</TableCell>
                      <TableCell>{item?.color}</TableCell>
                    </TableRow>
                  </TableBody>
                </Table>
              </div>
            </div>

            <div className="space-y-4">
              <h3 className="text-lg font-semibold text-slate-900">Description</h3>
              <p className="text-slate-600 leading-relaxed">{item?.description}</p>
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
