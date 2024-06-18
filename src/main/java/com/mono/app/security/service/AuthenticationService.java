package com.mono.app.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mono.app.dto.request.SignInRequest;
import com.mono.app.dto.request.SignUpRequest;
import com.mono.app.dto.response.AuthResponse;
import com.mono.app.exceptions.SecurityFault;
import com.mono.app.exceptions.types.ErrorCode;
import com.mono.app.exceptions.types.ErrorType;
import com.mono.app.model.Token;
import com.mono.app.model.User;
import com.mono.app.model.enums.RoleType;
import com.mono.app.model.enums.TokenType;
import com.mono.app.security.jwt.JwtAuthenticationService;
import com.mono.app.security.UserDetailsImpl;
import com.mono.app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;
    private final RoleService roleService;
    private final UserService userService;
    
    public User signUp(SignUpRequest request) {
        User existingUser = userService.getUserByEmail(request.email());

        if (existingUser != null) {
            throw new SecurityFault(
                    "The specified email is currently in use",
                    ErrorCode.S002,
                    ErrorType.SECURITY_ERROR);
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(roleService.getRole(RoleType.USER))
                .isEnabled(true)
                .build();

        return userService.saveUser(user);
    }
    
    public AuthResponse signIn(SignInRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (AuthenticationException e) {
            throw new SecurityFault(
                    "Incorrect email or password",
                    ErrorCode.S001,
                    ErrorType.SECURITY_ERROR);
        }

        User user = userService.getUserByEmail(request.email());

        List<SimpleGrantedAuthority> authorities = roleService.getAuthorities(user.getId());

        UserDetailsImpl userDetails = new UserDetailsImpl(user.getEmail(), user.getPassword(), user.getIsEnabled(), authorities);

        String accessToken = jwtAuthenticationService.generateAccessToken(userDetails);
        String refreshToken = jwtAuthenticationService.generateRefreshToken(userDetails);

        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        return new AuthResponse(accessToken, refreshToken);
    }

    // TODO: 2/13/2024 Добавить метод для отзыва токенов через endpoint
    
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String email;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) return;

        refreshToken = authHeader.substring(7);
        email = jwtAuthenticationService.extractUsername(refreshToken);

        if (email != null) {
            User user = userService.getUserByEmail(email);

            List<SimpleGrantedAuthority> authorities = roleService.getAuthorities(user.getId());

            UserDetailsImpl userDetails = new UserDetailsImpl(user.getEmail(), user.getPassword(), user.getIsEnabled(), authorities);

            if (jwtAuthenticationService.isTokenValid(refreshToken, userDetails)) {
                String accessToken = jwtAuthenticationService.generateAccessToken(userDetails);

                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);

                AuthResponse authResponse = new AuthResponse(accessToken, refreshToken);

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
    
    private void saveUserToken(User user, String accessToken) {
        Token token = Token.builder()
                .user(user)
                .token(accessToken)
                .type(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenService.saveToken(token);
    }
    
    private void revokeAllUserTokens(User user) {
        List<Token> tokens = tokenService.getAllValidTokens(user.getId());

        if (tokens.isEmpty()) return;

        tokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenService.saveAllTokens(tokens);
    }
}
