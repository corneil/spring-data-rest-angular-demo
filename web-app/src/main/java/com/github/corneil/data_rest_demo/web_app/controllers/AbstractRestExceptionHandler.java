package com.github.corneil.data_rest_demo.web_app.controllers;

import com.github.corneil.data_rest_demo.web_app.dto.ErrorMessage;
import com.github.corneil.data_rest_demo.web_app.util.ErrorHelper;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Corneil du Plessis.
 */
public class AbstractRestExceptionHandler {
    private Logger logger;
    public AbstractRestExceptionHandler(Logger logger) {
        this.logger = logger;
    }
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage handBindException(BindException x) {
        logger.error("handleBindException:{}", x.toString(), x);
        BindingResult errors = x.getBindingResult();
        if (errors == null) {
            logger.error("handleBindException:expected bindResult");
        } else {
            if (errors.hasErrors()) {
                StringBuilder message = new StringBuilder();
                if (errors.hasGlobalErrors()) {
                    for (ObjectError err : errors.getGlobalErrors()) {
                        if (message.length() > 0) {
                            message.append(',');
                        }
                        message.append(String.format("%s-%s", err.getCode(), err.getDefaultMessage()));
                        logger.debug(String.format("Error:%s-%s:%s", err.getCode(), err.getDefaultMessage(), err.toString()));
                    }
                }
                if (errors.hasFieldErrors()) {
                    for (FieldError err : errors.getFieldErrors()) {
                        if (message.length() > 0) {
                            message.append(',');
                        }
                        if (err.getObjectName() != null && !err.getObjectName().equals("object")) {
                            message.append(err.getObjectName());
                            message.append('.');
                        }
                        if (err.getField() != null) {
                            message.append(err.getField());
                        }
                        message.append('-');
                        message.append(err.getDefaultMessage());
                        logger.debug(String.format("Error:%s.%s-%s:%s",
                                err.getObjectName(),
                                err.getField(),
                                err.getDefaultMessage(),
                                err.toString()));
                    }
                }
                return new ErrorMessage(message.toString(), ErrorHelper.stackTrace(x));
            } else {
                logger.error("handleBindException:BindException:no errors");
            }
        }
        return new ErrorMessage(x.toString(), ErrorHelper.stackTrace(x));
    }
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage handleThrowableException(Throwable x) {
        logger.error("handleThrowableException:{}", x.toString(), x);
        return new ErrorMessage(x.getMessage(), ErrorHelper.stackTrace(x));
    }
}
