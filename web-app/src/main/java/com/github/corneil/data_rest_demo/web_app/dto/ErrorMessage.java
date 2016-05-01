package com.github.corneil.data_rest_demo.web_app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Corneil du Plessis.
 */
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessage {
    private String message;
    private String stackTrace;
    public ErrorMessage(String message, String stackTrace) {
        this.message = message;
        this.stackTrace = stackTrace;
    }
    public ErrorMessage(String message) {
        this.message = message;
    }
}
