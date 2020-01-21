package com.orez.nestedsettree.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DeleteNodeFailedException extends RuntimeException {

    public DeleteNodeFailedException() {
    }

    public DeleteNodeFailedException(String message) {
        super(message);
    }

    public DeleteNodeFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteNodeFailedException(Throwable cause) {
        super(cause);
    }
}
