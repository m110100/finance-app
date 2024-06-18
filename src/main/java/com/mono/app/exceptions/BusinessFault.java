package com.mono.app.exceptions;

import com.mono.app.exceptions.types.ErrorCode;
import com.mono.app.exceptions.types.ErrorType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessFault extends RuntimeException {
    private final ErrorCode code;
    private final ErrorType type;
    private String faultMessage;

    public BusinessFault(String faultMessage, ErrorCode code, ErrorType type) {
        super(faultMessage);

        this.faultMessage = faultMessage;
        this.code = code;
        this.type = type;
    }
}
