package com.phasezero.catalog.util;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String code;
    private String path;
    private List<FieldErrorDetail> details;

    public ErrorResponse() {}

    public ErrorResponse(LocalDateTime timestamp, int status, String error,
                         String message, String code, String path,
                         List<FieldErrorDetail> details) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.code = code;
        this.path = path;
        this.details = details;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public List<FieldErrorDetail> getDetails() { return details; }
    public void setDetails(List<FieldErrorDetail> details) { this.details = details; }

    @Setter
    @Getter
    public static class FieldErrorDetail {
        private String field;
        private String message;

        public FieldErrorDetail() {}

        public FieldErrorDetail(String field, String message) {
            this.field = field;
            this.message = message;
        }

    }
}
