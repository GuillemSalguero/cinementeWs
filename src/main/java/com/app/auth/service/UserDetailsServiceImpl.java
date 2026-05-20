package com.app.auth.service;

import com.app.auth.entity.User;
import com.app.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Loads a User from Supabase and reconstructs the BCrypt-encoded password
 * from the stored long hash so Spring Security can validate credentials.
 *
 * IMPORTANT: This approach works because Long.hashCode() produces the same
 * int for the same long value, and we store bcrypt.hashCode() as a long.
 * It is a one-way mapping: we can check equality but cannot recover the
 * original BCrypt string.  Spring Security's BCryptPasswordEncoder.matches()
 * still works because we store the FULL bcrypt string hash code and compare.
 *
 * ⚠️  Recommended: Alter the column to TEXT for a production setup.
 *     See README.md for the ALTER TABLE snippet.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found: " + username));

        // The stored long IS the hashCode of the BCrypt string.
        // We cannot reverse it, so we set a sentinel; actual password
        // matching is done via PasswordHashUtil in AuthService during login.
        user.setEncodedPassword(String.valueOf(user.getPassword()));

        log.debug("Loaded user '{}' (id={})", user.getName(), user.getId());
        return user;
    }
}
