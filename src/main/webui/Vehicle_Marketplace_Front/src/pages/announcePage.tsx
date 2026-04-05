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
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { ECategory } from "@/enums/ECategory";
import { EColors } from "@/enums/EColors";
import { EFuelType } from "@/enums/EFuelType";
import { EVehicleStatus } from "@/enums/EVehicleStatus";
import {
  createVehicleWithDocs,
  createOneVehicle,
} from "@/services/requests/vehiclesRequest";
import logo from "../assets/logo/horse_power_vehicle_logo.png";
import { isAuthenticated } from "@/services/utils";

export const AnnouncePage = () => {
  const vehicleKinds: Map<string, string> = new Map([
    ["Bike", "bikes"],
    ["Boat", "boats"],
    ["Car", "cars"],
    ["Plane", "planes"],
  ]);

  if (!isAuthenticated()) {
    sessionStorage.setItem("redirectAfterLogin", window.location.pathname);
    window.location.href = "/enter";
    return;
  }

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const form = e.currentTarget;
    const formData = new FormData(form);
    const data: any = Object.fromEntries(formData.entries());

    const fileInput = form.querySelector(
      'input[type="file"]',
    ) as HTMLInputElement;
    const files = fileInput?.files;

    try {
      if (files && files.length > 0) {
        const vehicleData: { [key: string]: any } = {};
        formData.forEach((value, key) => {
          if (key !== "files") {
            vehicleData[key] = value;
          }
        });

        if (vehicleData.year) vehicleData.year = Number(vehicleData.year);

        const uploadFormData = new FormData();
        uploadFormData.append("vehicles", JSON.stringify(vehicleData));

        for (const file of files) {
          uploadFormData.append("files", file);
        }

        const response = await createVehicleWithDocs(data.kind, uploadFormData);
        console.log(response);
      } else {
        delete data.files; // Remove empty file entry if present
        if (data.year) data.year = Number(data.year);
        const response = await createOneVehicle(data.kind, data);
        console.log(response);
      }
    } catch (error) {
      console.error("Error creating vehicle:", error);
    }
  };

  return (
    <div
      className="min-h-screen bg-slate-50 py-12 px-4 sm:px-6 lg:px-8"
      style={{
        backgroundImage: `url(${logo})`,
        backgroundSize: "contain",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat",
        backgroundAttachment: "fixed",
      }}
    >
      <div className="container mx-auto max-w-4xl">
        <Card className="shadow-lg border-slate-200 bg-white/80 backdrop-blur-sm">
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
                    <Select name="kind">
                      <SelectTrigger
                        aria-label="kind"
                        className="border-slate-400 mx-auto mt-0.5"
                      >
                        <SelectValue placeholder="Select Kind" />
                      </SelectTrigger>
                      <SelectContent>
                        {Array.from(vehicleKinds.entries()).map(
                          ([key, value]) => (
                            <SelectItem key={key} value={value}>
                              {key}
                            </SelectItem>
                          ),
                        )}
                      </SelectContent>
                    </Select>
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Vehicle Name
                    </label>
                    <Input
                      placeholder="e.g. Yamaha MT-07"
                      name="name"
                      aria-label="name"
                      className="border-slate-400"
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
                      className="border-slate-400"
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
                      className="border-slate-400"
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
                      className="border-slate-400"
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
                      className="border-slate-400"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Category
                    </label>
                    <Select name="category">
                      <SelectTrigger
                        aria-label="category"
                        className="border-slate-400 mx-auto mt-0.5"
                      >
                        <SelectValue placeholder="Select Category" />
                      </SelectTrigger>
                      <SelectContent>
                        {Object.keys(ECategory)
                          .filter((k) => isNaN(Number(k)))
                          .map((category) => (
                            <SelectItem key={category} value={category}>
                              {category}
                            </SelectItem>
                          ))}
                      </SelectContent>
                    </Select>
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
                      className="border-slate-400"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Transmission
                    </label>
                    <Select name="transmission">
                      <SelectTrigger
                        aria-label="transmissionType"
                        className="border-slate-400 mx-auto mt-0.5"
                      >
                        <SelectValue placeholder="Select Transmission" />
                      </SelectTrigger>
                      <SelectContent>
                        {["Manual", "Automatic", "Semi-Automatic", "CVT"].map(
                          (type) => (
                            <SelectItem key={type} value={type}>
                              {type}
                            </SelectItem>
                          ),
                        )}
                      </SelectContent>
                    </Select>
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Fuel Type
                    </label>
                    <Select name="fuelType">
                      <SelectTrigger
                        aria-label="fuelType"
                        className="border-slate-400 mx-auto mt-0.5"
                      >
                        <SelectValue placeholder="Select Fuel Type" />
                      </SelectTrigger>
                      <SelectContent>
                        {Object.keys(EFuelType)
                          .filter((k) => isNaN(Number(k)))
                          .map((fuel) => (
                            <SelectItem key={fuel} value={fuel}>
                              {fuel}
                            </SelectItem>
                          ))}
                      </SelectContent>
                    </Select>
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Color
                    </label>
                    <Select name="color">
                      <SelectTrigger
                        aria-label="color"
                        className="border-slate-400 mx-auto mt-0.5"
                      >
                        <SelectValue placeholder="Select Color" />
                      </SelectTrigger>
                      <SelectContent>
                        {Object.keys(EColors)
                          .filter((k) => isNaN(Number(k)))
                          .map((color) => (
                            <SelectItem key={color} value={color}>
                              {color}
                            </SelectItem>
                          ))}
                      </SelectContent>
                    </Select>
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Storage / Capacity
                    </label>
                    <Input
                      placeholder="e.g. 14L"
                      name="storage"
                      aria-label="storage"
                      className="border-slate-400"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-slate-900">
                      Condition
                    </label>
                    <Select name="vehicleStatus">
                      <SelectTrigger
                        aria-label="vehicleStatus"
                        className="border-slate-400 mx-auto mt-0.5"
                      >
                        <SelectValue placeholder="Select Condition" />
                      </SelectTrigger>
                      <SelectContent>
                        {Object.keys(EVehicleStatus)
                          .filter((k) => isNaN(Number(k)))
                          .map((status) => (
                            <SelectItem key={status} value={status}>
                              {status}
                            </SelectItem>
                          ))}
                      </SelectContent>
                    </Select>
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
                    className="flex min-h-[120px] w-full rounded-md border border-slate-400 bg-white px-3 py-2 text-sm ring-offset-white placeholder:text-slate-500 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-slate-950 focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
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
                    className="cursor-pointer border-slate-400 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-slate-100 file:text-slate-700 hover:file:bg-slate-200"
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
