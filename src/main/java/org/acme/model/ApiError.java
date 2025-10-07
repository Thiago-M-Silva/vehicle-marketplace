package org.acme.model;

import java.time.Instant;

public class ApiError {
    public Instant timestamp = Instant.now();
    public int status;
    public String error;
    public String code;
    public String message;
    public String path;

    public ApiError(int status, String error, String code, String message, String path) {
        this.status = status;
        this.error = error;
        this.code = code;
        this.message = message;
        this.path = path;
    }
}
