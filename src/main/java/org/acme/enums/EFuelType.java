package org.acme.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum EFuelType {
    DIESEL,
    ELECTRIC,
    HYBRID,
    LPG,
    CNG,
    ETHANOL,
    BIODIESEL,
    PROPANE,
    BUTANE,
    FUEL_OIL,
    JET_FUEL,
    KEROSENE,
    ALCOHOL,
    GASOLINE;
}
