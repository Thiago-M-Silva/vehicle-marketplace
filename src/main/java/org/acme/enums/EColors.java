package org.acme.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum EColors {
    RED,
    GREEN,
    BLUE,
    YELLOW,
    BLACK,
    WHITE,
    ORANGE,
    PURPLE,
    PINK,
    BROWN,
    GRAY,
    CYAN,
    MAGENTA,
    TEAL,
    MAROON,
    NAVY,
    OLIVE,
    SILVER,
    GOLD;
    // Add more colors as needed
}
