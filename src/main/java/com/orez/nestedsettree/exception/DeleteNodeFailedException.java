package com.orez.nestedsettree.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DeleteNodeFailedException extends RuntimeException {
    public DeleteNodeFailedException(String message) {
        super(message);
    }
}
