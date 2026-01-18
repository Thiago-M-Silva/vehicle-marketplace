import bikeWebp from "../assets/bike/horse_power_vehicle_moto.webp";
import boatWebp from "../assets/boat/horse_power_vehicle_boat.webp";
import carWebp from "../assets/car/horse_power_vehicle_car.webp";
import planeWebp from "../assets/plane/horse_power_vehicle_aircraft.webp";

export const dataToShowInResume = (kind: string): any[] => {
  const values: any[] = [];
  
  switch (kind) {
    case "bikes":
      values.push(
        {
          name: "bike1",
          description: "See some of our most popular motorbikes.",
          image: bikeWebp
        },
        {
          name: "bike2",
          description: "See some of our most popular boats.",
          image: bikeWebp
        },
        {
          name: "bike3",
          description: "See some of our most popular cars.",
          image: bikeWebp
        },
        {
          name: "bike4",
          description: "See some of our most popular planes.",
          image: bikeWebp
        },
        {
          name: "bike5",
          description: "See some of our most popular planes.",
          image: bikeWebp
        },
        {
          name: "bike6",
          description: "See some of our most popular planes.",
          image: bikeWebp
        }
      );
      break;
    case "boats":
      values.push(
        {
          name: "boat1",
          description: "See some of our most popular motorboats.",
          image: boatWebp
        },
        {
          name: "boat2",
          description: "See some of our most popular boats.",
          image: boatWebp
        },
        {
          name: "boat3",
          description: "See some of our most popular cars.",
          image: boatWebp
        },
        {
          name: "boat4",
          description: "See some of our most popular planes.",
          image: boatWebp
        },
        {
          name: "boat5",
          description: "See some of our most popular planes.",
          image: boatWebp
        },
        {
          name: "boat6",
          description: "See some of our most popular planes.",
          image: boatWebp
        }
      );
      break;
    case "cars":
      values.push(
        {
          name: "cars1",
          description: "See some of our most popular cars.",
          image: carWebp
        },
        {
          name: "cars2",
          description: "See some of our most popular boats.",
          image: carWebp
        },
        {
          name: "cars3",
          description: "See some of our most popular cars.",
          image: carWebp
        },
        {
          name: "cars4",
          description: "See some of our most popular planes.",
          image: carWebp
        },
        {
          name: "cars5",
          description: "See some of our most popular planes.",
          image: carWebp
        },
        {
          name: "cars6",
          description: "See some of our most popular planes.",
          image: carWebp
        }
      );
      break;
    case "planes":
      values.push(
        {
          name: "plane1",
          description: "See some of our most popular motorbikes.",
          image: planeWebp
        },
        {
          name: "plane2",
          description: "See some of our most popular boats.",
          image: planeWebp
        },
        {
          name: "plane3",
          description: "See some of our most popular cars.",
          image: planeWebp
        },
        {
          name: "plane4",
          description: "See some of our most popular planes.",
          image: planeWebp
        },
        {
          name: "plane5",
          description: "See some of our most popular planes.",
          image: planeWebp
        },
        {
          name: "plane6",
          description: "See some of our most popular planes.",
          image: planeWebp
        }
      );
      break;
    default:
      break;
  }

  return values;
};
