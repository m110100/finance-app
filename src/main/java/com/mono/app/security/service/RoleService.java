package com.mono.app.security.service;

import com.mono.app.exceptions.BusinessFault;
import com.mono.app.exceptions.types.ErrorCode;
import com.mono.app.exceptions.types.ErrorType;
import com.mono.app.model.Role;
import com.mono.app.model.enums.RoleType;
import com.mono.app.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getRole(RoleType name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new BusinessFault(
                        String.format("Роль с названием %s не найдена", name),
                        ErrorCode.E001, ErrorType.INFO));
    }

    /**
     * Получает список разрешений (permissions) в форме SimpleGrantedAuthority для пользователя с указанным идентификатором.
     *
     * @param userId Идентификатор пользователя.
     * @return Список разрешений пользователя, включая его роль, в форме SimpleGrantedAuthority.
     */
    public List<SimpleGrantedAuthority> getAuthorities(Long userId) {
        Role role = roleRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessFault(
                        String.format("Роль с указанным userId:  %s не найдена", userId),
                        ErrorCode.E001, ErrorType.ERROR));

        // ? Добавляет все полномочия у конкретной роли
        List<SimpleGrantedAuthority> authorities = role.getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(toList());

        // ? Непосредственно добавляет роль пользователя
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

        return authorities;
    }
}
