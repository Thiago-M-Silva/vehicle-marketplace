package org.acme.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum EStatus {
    NEW,
    USED,
    CERTIFIED_PRE_OWNED,
    DAMAGED,
    SALVAGE,
    REBUILT,
    JUNK,
    SCRAP,
    PARTS_ONLY,
    FLOOD_DAMAGE,
    HAIL_DAMAGE,
    ACCIDENT_DAMAGE,
    THEFT_RECOVERY,
    MECHANICAL_ISSUES,
    FRAME_DAMAGE,
    AIRBAG_DEPLOYED,
    FIRE_DAMAGE
    // Add more statuses as needed
}
