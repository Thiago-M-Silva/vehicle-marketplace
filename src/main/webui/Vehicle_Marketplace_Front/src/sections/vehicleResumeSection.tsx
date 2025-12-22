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
import { dataToShowInResume } from "@/utils/tempMethods";
import { useState } from "react";

type Props = {};

export const VehicleResumeSection = ({}: Props) => {
  const [tabValue, setTabValue] = useState("bikes");

  console.log(tabValue);
  const values = dataToShowInResume(tabValue);

  console.log(values);
  return (
    <div className="bg-gray-400 mx-10 py-10 rounded-lg">
      <Tabs
        defaultValue="bikes"
        className="w-[90%] mx-auto mt-2 px-2 border-2 border-amber-400 "
      >
        <TabsList className="flex justify-between w-[85%] mx-auto">
          <TabsTrigger value="bikes" onClick={() => setTabValue("bikes")}>
            Motorbikes
          </TabsTrigger>
          <TabsTrigger value="boats" onClick={() => setTabValue("boats")}>
            Boats
          </TabsTrigger>
          <TabsTrigger value="cars" onClick={() => setTabValue("cars")}>
            Cars
          </TabsTrigger>
          <TabsTrigger value="planes" onClick={() => setTabValue("planes")}>
            Planes
          </TabsTrigger>
        </TabsList>
        <TabsContent value={tabValue}>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
            {values.map((value: any, index: number) => (
              <Card key={index}>
                <CardHeader>
                  <CardTitle>{value.name}</CardTitle>
                  <CardDescription>{value.description}</CardDescription>
                </CardHeader>
                <CardContent>
                  <div>{value.name}</div>
                  <div>
                    <img src="" alt="motorbike_image" />
                  </div>
                  <div>{value.description}</div>
                </CardContent>
                <CardFooter className="justify-center">
                  <Button>See more</Button>
                </CardFooter>
              </Card>
            ))}
          </div>
        </TabsContent>
      </Tabs>
    </div>
  );
};
