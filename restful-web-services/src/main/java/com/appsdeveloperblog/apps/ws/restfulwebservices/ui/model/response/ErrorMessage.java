package com.appsdeveloperblog.apps.ws.restfulwebservices.ui.model.response;

import java.time.LocalDateTime;

public class ErrorMessage {
    private LocalDateTime timestamp;
    private String message;
    private String status;
    private Integer statusCode;

    public ErrorMessage() {
    }

    public ErrorMessage(LocalDateTime timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }

    public ErrorMessage(LocalDateTime timestamp, String message, String status, Integer statusCode) {
        this.timestamp = timestamp;
        this.message = message;
        this.status = status;
        this.statusCode = statusCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
