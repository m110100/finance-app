package com.mono.app.utils.security;

import com.mono.app.model.User;
import com.mono.app.service.UserService;
import com.mono.app.utils.security.types.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserResolver {
    private final AuthenticationFacade authenticationFacade;
    private final UserService userService;

    public User getAuthenticatedUser() {
        // TODO: 2/14/2024 Нужна ли обработка? По текущей моей осведомленности нет.
        //  Так как authentication не может быть null в контексте моей настройки Spring Security
        Authentication authentication = authenticationFacade.getAuthentication();

        return userService.getUserByEmail(authenticationFacade.getAuthentication().getName());
    }
}
