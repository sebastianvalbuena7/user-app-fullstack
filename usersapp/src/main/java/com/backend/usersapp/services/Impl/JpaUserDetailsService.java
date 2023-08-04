package com.backend.usersapp.services.Impl;

import com.backend.usersapp.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.backend.usersapp.models.entities.User> userFind = userRepository.findByUsername(username);
        if (userFind.isEmpty()) {
            throw new UsernameNotFoundException(String.format("Username %s not exist", username));
        }
        com.backend.usersapp.models.entities.User user = userFind.orElseThrow();

        List<GrantedAuthority> authorities = user.getRoles()
                .stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new User(user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }
}