package com.app.auth.security;

import com.app.auth.entity.User;
import com.app.auth.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserHelper {

    private final UserRepository userRepository;

    public AuthenticatedUserHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** Devuelve el User autenticado leyendo el email del SecurityContext. */
    public User currentUser() {
        UserDetails principal = (UserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        return userRepository.findByName(principal.getUsername())
                .orElseThrow(() -> new IllegalStateException("Usuario autenticado no encontrado en BD"));
    }

    public Long currentUserId() {
        return currentUser().getId();
    }
}
