package com.mono.app.dto.request;

public record SignInRequest(
        String email,
        String password
) { }
