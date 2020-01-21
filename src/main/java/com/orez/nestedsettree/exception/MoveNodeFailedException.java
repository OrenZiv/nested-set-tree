package com.orez.nestedsettree.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MoveNodeFailedException extends RuntimeException {

    public MoveNodeFailedException() {
    }

    public MoveNodeFailedException(String message) {
        super(message);
    }

    public MoveNodeFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MoveNodeFailedException(Throwable cause) {
        super(cause);
    }
}
