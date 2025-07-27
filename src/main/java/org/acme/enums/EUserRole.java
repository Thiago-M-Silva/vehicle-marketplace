package org.acme.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum EUserRole {
    ADMIN,
    CLIENT,
    TECHNICIAN;
}
