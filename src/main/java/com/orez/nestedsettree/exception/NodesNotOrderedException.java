package com.orez.nestedsettree.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NodesNotOrderedException extends IllegalStateException {
    public NodesNotOrderedException(String s) {
        super(s);
    }
}
