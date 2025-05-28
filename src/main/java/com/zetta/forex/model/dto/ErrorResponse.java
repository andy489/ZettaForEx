package com.zetta.forex.model.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public ErrorResponse(String message) {
        this.timestamp = LocalDateTime.now();
        this.status = 400; // Bad Request
        this.error = "Bad Request";
        this.message = message;
    }

    // More detailed constructor
    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
