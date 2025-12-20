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

type Props = {
 
}

export const VehicleResumeSection = ({}: Props) => {
    const [tabValue, setTabValue] = useState("bikes");

    console.log(tabValue);
    const values = dataToShowInResume(tabValue);

    console.log(values);
    return ( 
        <div>
            <Tabs
                defaultValue="bikes"
                className="w-[70%] mx-auto mt-2 px-2 border-2 border-amber-400 "
            >
                <TabsList className="flex justify-between">
                    <TabsTrigger value="bikes" onClick={() => setTabValue('bikes')}>Motorbikes</TabsTrigger>
                    <TabsTrigger value="boats" onClick={() => setTabValue('boats')}>Boats</TabsTrigger>
                    <TabsTrigger value="cars" onClick={() => setTabValue('cars')}>Cars</TabsTrigger>
                    <TabsTrigger value="planes" onClick={() => setTabValue('planes')}>Planes</TabsTrigger>
                </TabsList>
               {values.map((value: any) => (
                <TabsContent value={value}>
                    <Card>
                        <CardHeader>
                            <CardTitle>{value.name}</CardTitle>
                            <CardDescription>
                                {value.description}
                            </CardDescription>
                        </CardHeader>
                        <CardContent>
                            <div >
                                {value.name}
                            </div>
                            <div >
                                <img src="" alt="motorbike_image" />
                            </div>
                            <div >
                                {value.description}
                            </div>
                        </CardContent>
                        <CardFooter className="justify-center">
                            <Button>See more</Button>
                        </CardFooter>
                    </Card>
                </TabsContent>
               ))}
            </Tabs>
        </div>
    );
}