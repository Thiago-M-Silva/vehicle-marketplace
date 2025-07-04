package org.acme.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum ECategory {
    SEDAN,
    SUV,
    TRUCK,
    MOTORCYCLE,
    VAN,
    COUPE,
    HATCHBACK,
    CONVERTIBLE,
    WAGON,
    SPORTS_CAR,
    ELECTRIC_CAR,
    HYBRID_CAR,
    LUXURY_CAR,
    PICKUP_TRUCK,
    CROSSOVER,
    MINIVAN,
    OFF_ROAD_VEHICLE,
    COMPACT_CAR,
    SUBCOMPACT_CAR,
    FULL_SIZE_CAR
}
