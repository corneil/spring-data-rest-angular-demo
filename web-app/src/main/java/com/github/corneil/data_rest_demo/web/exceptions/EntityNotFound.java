package com.github.corneil.data_rest_demo.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Corneil du Plessis.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Entity not found")
public class EntityNotFound extends Exception {
	public EntityNotFound() {
	}

	public EntityNotFound(String message) {
		super(message);
	}

	public EntityNotFound(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityNotFound(Throwable cause) {
		super(cause);
	}
}
