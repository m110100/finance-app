package com.mono.app.exceptions;

import com.mono.app.exceptions.types.ErrorCode;
import com.mono.app.exceptions.types.ErrorType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.AuthenticationException;

// TODO: 2/12/2024 Реализовать
@Getter
@Setter
public class SecurityFault extends AuthenticationException {
    private final ErrorCode code;
    private final ErrorType type;
    private String faultMessage;

    public SecurityFault(String faultMessage, ErrorCode code, ErrorType type) {
        super(faultMessage);

        this.faultMessage = faultMessage;
        this.code = code;
        this.type = type;
    }
}
