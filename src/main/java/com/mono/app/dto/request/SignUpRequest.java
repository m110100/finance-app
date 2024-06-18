package com.mono.app.dto.request;

public record SignUpRequest(
        String username,
        String email,
        String password
) { }
