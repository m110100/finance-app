package com.mono.app.configuration;

import com.mono.app.exceptions.BusinessFault;
import com.mono.app.exceptions.SecurityFault;
import com.mono.app.exceptions.types.ErrorCode;
import com.mono.app.exceptions.types.ErrorMessage;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Log4j
@RestControllerAdvice
public class DelegatedExceptionHandler {
    // TODO: 2/14/2024 Добавить обработку остальных кодов, когда появятся 
    @ExceptionHandler(BusinessFault.class)
    public ResponseEntity<ErrorMessage> businessException(BusinessFault e) {
        log.debug(String.format("%s: %s - %s", e.getType(), e.getCode(), e.getFaultMessage()));

        if (e.getCode() == ErrorCode.E001) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMessage(e.getFaultMessage()));
        } else {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ErrorMessage(e.getFaultMessage()));
        }
    }

    @ExceptionHandler(SecurityFault.class)
    public ResponseEntity<ErrorMessage> securityException(SecurityFault e) {
        if (e.getCode() == ErrorCode.S001) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorMessage(e.getFaultMessage()));
        } else if (e.getCode() == ErrorCode.S002) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ErrorMessage(e.getFaultMessage()));
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMessage(e.getFaultMessage()));
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> uncaughtException(RuntimeException e) {
        log.debug(String.format(e.getMessage()));

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage(e.getMessage()));
    }
}
