package com.mono.app.service;

import com.mono.app.exceptions.BusinessFault;
import com.mono.app.exceptions.SecurityFault;
import com.mono.app.exceptions.types.ErrorCode;
import com.mono.app.exceptions.types.ErrorType;
import com.mono.app.model.User;
import com.mono.app.repository.UserRepository;
import com.mono.app.utils.security.types.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserByEmail(String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        return existingUser.orElse(null);

    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
