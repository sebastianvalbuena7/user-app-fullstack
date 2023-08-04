package com.backend.usersapp.services.Impl;

import com.backend.usersapp.models.entities.Role;
import com.backend.usersapp.models.entities.User;
import com.backend.usersapp.models.request.UserRequest;
import com.backend.usersapp.repositories.RoleRepository;
import com.backend.usersapp.repositories.UserRepository;
import com.backend.usersapp.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<Role> o = roleRepository.findByName("ROLE_USER");

        List<Role> roles = new ArrayList<>();
        if (o.isPresent()) {
            roles.add(o.orElseThrow());
        }
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Override
    public Optional<User> update(UserRequest user, Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User userDb = userOptional.orElseThrow();
            userDb.setUsername(user.getUsername());
            userDb.setEmail(user.getEmail());
            return Optional.of(userRepository.save(userDb));
        }
        return Optional.empty();
    }

    @Override
    public void remove(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        userRepository.delete(user);
    }
}