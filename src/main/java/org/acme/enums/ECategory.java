package org.acme.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum ECategory {
    HATCH,
    SEDAN,
    SUV,
    CROSSOVER,
    MINIVAN,
    STATION_WAGON,
    CUPÊ,
    SPORTIVE,
    PICKUP,
    VAN,
    TRUCK,
    CRUISERS,
    SPORTBIKES,
    STANDARD,
    ADVENTURE,
    DIRTBIKES,
    ELETRIC,
    CHOPPERS,
    TOURING,
    VINTAGE,
    SCOOTER,
    ELETRIC_BIKE,
    QUADRICYCLE,
    MONOCYCLE,
    ALUMINIUM,
    BOW_RIDER,
    CONSOLE,
    DAYCRUISER,
    PILOTHOUSE,
    SPEEDBOAT,
    JET_SKY,
    JET,
    AMPHIBIOUS,
    TURBOPROP,
    SINGLE_ENGINE,
    TWIN_ENGINE,
    HELICOPTER
}
