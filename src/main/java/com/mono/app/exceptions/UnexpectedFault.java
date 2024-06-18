package com.mono.app.exceptions;

import lombok.Getter;

@Getter
public class UnexpectedFault extends Exception {
    private final String reason;

    public UnexpectedFault(String reason) {
        super(reason);
        this.reason = reason;
    }

    public UnexpectedFault(Throwable cause) {
        super(cause);
        this.reason = cause.getMessage();
    }
}
