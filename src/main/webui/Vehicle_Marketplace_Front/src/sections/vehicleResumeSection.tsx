import { Tabs, TabsList, TabsTrigger, TabsContent } from "@radix-ui/react-tabs";
import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
  CardFooter,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { dataToShowInResume } from "@/services/tempMethods";
import { useState } from "react";
import { redirectMethod } from "@/services/utils";

export const VehicleResumeSection = () => {
  const [tabValue, setTabValue] = useState("bikes");

  const values = dataToShowInResume(tabValue);
  const categories = ["bikes", "boats", "cars", "planes"];

  return (
    <section className="w-full py-12 bg-slate-50">
      <div className="container mx-auto px-4">
        <div className="text-center mb-10">
          <h2 className="text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">
            Featured Vehicles
          </h2>
          <p className="mt-4 text-lg text-gray-600">
            Browse our selection of premium vehicles across all categories.
          </p>
        </div>

        <Tabs value={tabValue} onValueChange={setTabValue} className="w-full">
          <TabsList className="flex w-full max-w-md mx-auto h-12 items-center justify-center rounded-lg bg-slate-200 p-1 text-slate-600">
            {categories.map((category) => (
              <TabsTrigger
                key={category}
                value={category}
                className="flex-1 inline-flex items-center justify-center whitespace-nowrap rounded-md px-3 py-2 text-sm font-medium ring-offset-white transition-all focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-slate-400 focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 data-[state=active]:bg-white data-[state=active]:text-slate-950 data-[state=active]:shadow capitalize"
              >
                {category}
              </TabsTrigger>
            ))}
          </TabsList>
          <TabsContent
            value={tabValue}
            className="mt-8 focus-visible:outline-none"
          >
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {values.map((value: any, index: number) => (
                <Card
                  key={index}
                  className="overflow-hidden hover:shadow-lg transition-shadow duration-300 flex flex-col"
                >
                  <div className="aspect-video w-full overflow-hidden bg-gray-100 relative">
                    <img
                      src={value.image}
                      alt={value.name}
                      className="object-cover w-full h-full transition-transform duration-500 hover:scale-105"
                    />
                  </div>
                  <CardHeader className="pb-2">
                    <CardTitle className="text-xl">{value.name}</CardTitle>
                  </CardHeader>
                  <CardContent className="flex-grow">
                    <CardDescription className="text-sm text-gray-600 line-clamp-3">
                      {value.description}
                    </CardDescription>
                  </CardContent>
                  <CardFooter className="pt-4">
                    <Button className="w-full" onClick={() => redirectMethod('/productInfo')}>See more</Button>
                  </CardFooter>
                </Card>
              ))}
            </div>
          </TabsContent>
        </Tabs>
      </div>
    </section>
  );
};
