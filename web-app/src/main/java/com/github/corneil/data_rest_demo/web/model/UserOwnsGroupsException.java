package com.github.corneil.data_rest_demo.web.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Corneil on 2016-05-04.
 */
@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class UserOwnsGroupsException extends RuntimeException {
    public UserOwnsGroupsException() {
    }
    public UserOwnsGroupsException(Throwable cause) {
        super(cause);
    }
    public UserOwnsGroupsException(String message) {
        super(message);
    }
    public UserOwnsGroupsException(String message, Throwable cause) {
        super(message, cause);
    }
    public UserOwnsGroupsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
