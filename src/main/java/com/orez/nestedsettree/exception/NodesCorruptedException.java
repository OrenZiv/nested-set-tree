package com.orez.nestedsettree.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NodesCorruptedException extends IllegalStateException {

    public NodesCorruptedException() {
    }

    public NodesCorruptedException(String s) {
        super(s);
    }

    public NodesCorruptedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NodesCorruptedException(Throwable cause) {
        super(cause);
    }
}
