import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  createVehicleWithDocs,
  createOneVehicle,
} from "@/services/requests/vehiclesRequest";

export const AnnouncePage = () => {
  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const formData = new FormData(e.currentTarget);
    const data: any = Object.fromEntries(formData.entries());

    // Convert year to number if present
    if (data.year) {
      data.year = Number(data.year);
    }

    const fileInput = e.currentTarget.querySelector(
      'input[type="file"]',
    ) as HTMLInputElement;
    const files = fileInput?.files;

    try {
      if (files && files.length > 0) {
        // If files are present, use createVehicleWithDocs
        const payload = { ...data, files };
        const response = await createVehicleWithDocs(payload.kind, payload);
        console.log(response);
      } else {
        delete data.files; // Remove empty file entry if present
        const response = await createOneVehicle(data.kind, data);
        console.log(response);
      }
    } catch (error) {
      console.error("Error creating vehicle:", error);
    }
  };

  return (
    <div className="min-h-screen bg-slate-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="container mx-auto max-w-4xl">
        <Card className="shadow-lg border-slate-200">
          <CardHeader className="text-center sm:text-left">
            <CardTitle className="text-3xl font-bold tracking-tight text-slate-900">
              Announce Your Vehicle
            </CardTitle>
            <CardDescription className="text-lg mt-2">
              Fill in the details below to list your vehicle for sale on the
              marketplace.
            </CardDescription>
          </CardHeader>
          <CardContent>
            <form className="space-y-8" onSubmit={handleSubmit}>
              {/* Section: Basic Info */}
              <div className="space-y-4">
                <h3 className="text-xl font-semibold text-slate-900 border-b pb-2">
                  Basic Information
                </h3>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Vehicle Kind
                    </label>
                    <Input
                      placeholder="e.g. Bike, Boat, Car, Plane"
                      name="kind"
                      aria-label="kind"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Vehicle Name
                    </label>
                    <Input
                      placeholder="e.g. Yamaha MT-07"
                      name="name"
                      aria-label="name"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Brand
                    </label>
                    <Input
                      placeholder="e.g. Yamaha"
                      name="brand"
                      aria-label="brand"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Model
                    </label>
                    <Input
                      placeholder="e.g. MT-07"
                      name="model"
                      aria-label="model"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Year
                    </label>
                    <Input
                      type="number"
                      placeholder="e.g. 2024"
                      name="year"
                      aria-label="year"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Price
                    </label>
                    <Input
                      placeholder="$ 0.00"
                      name="price"
                      aria-label="price"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Category
                    </label>
                    <Input
                      placeholder="e.g. Motorcycle"
                      name="category"
                      aria-label="category"
                    />
                  </div>
                </div>
              </div>

              {/* Section: Specs */}
              <div className="space-y-4">
                <h3 className="text-xl font-semibold text-slate-900 border-b pb-2">
                  Technical Specifications
                </h3>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Horsepower
                    </label>
                    <Input
                      placeholder="e.g. 75 hp"
                      name="horsePower"
                      aria-label="horsepower"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Transmission
                    </label>
                    <Input
                      placeholder="e.g. Manual"
                      name="transmission"
                      aria-label="transmissionType"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Fuel Type
                    </label>
                    <Input
                      placeholder="e.g. Gasoline"
                      name="fuelType"
                      aria-label="fuelType"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Color
                    </label>
                    <Input
                      placeholder="e.g. Blue"
                      name="color"
                      aria-label="color"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Storage / Capacity
                    </label>
                    <Input
                      placeholder="e.g. 14L"
                      name="storage"
                      aria-label="storage"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Condition
                    </label>
                    <Input
                      placeholder="e.g. Used"
                      name="vehicleStatus"
                      aria-label="vehicleStatus"
                    />
                  </div>
                </div>
              </div>

              {/* Section: Details */}
              <div className="space-y-4">
                <h3 className="text-xl font-semibold text-slate-900 border-b pb-2">
                  Details
                </h3>
                <div className="space-y-2">
                  <label className="text-sm font-medium text-slate-900">
                    Description
                  </label>
                  <textarea
                    className="flex min-h-[120px] w-full rounded-md border border-slate-200 bg-white px-3 py-2 text-sm ring-offset-white placeholder:text-slate-500 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-slate-950 focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
                    placeholder="Provide a detailed description of the vehicle..."
                    name="description"
                    aria-label="description"
                  />
                </div>
              </div>

              {/* Section: Documents */}
              <div className="space-y-4">
                <h3 className="text-xl font-semibold text-slate-900 border-b pb-2">
                  Documents & Images
                </h3>
                <div className="space-y-2">
                  <label className="text-sm font-medium text-slate-900">
                    Upload Files
                  </label>
                  <Input
                    type="file"
                    className="cursor-pointer file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-slate-100 file:text-slate-700 hover:file:bg-slate-200"
                    name="files"
                    aria-label="docs"
                    multiple
                  />
                  <p className="text-xs text-slate-500">
                    Upload vehicle images and relevant documentation.
                  </p>
                </div>
              </div>

              <div className="pt-6">
                <Button className="w-full text-lg py-6" type="submit">
                  Publish Announcement
                </Button>
              </div>
            </form>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};
