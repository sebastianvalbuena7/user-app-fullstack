package com.backend.usersapp.services.Impl;

import com.backend.usersapp.models.entities.Role;
import com.backend.usersapp.models.entities.User;
import com.backend.usersapp.models.request.UserRequest;
import com.backend.usersapp.models.response.UserDTO;
import com.backend.usersapp.models.response.mapper.DtoMapper;
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
    public List<UserDTO> findAll() {
        List<User> users = (List<User>) userRepository.findAll();
        return users
                .stream()
                .map(user -> DtoMapper.builder().setUser(user).build())
                .toList();
    }

    @Override
    public Optional<UserDTO> findById(Long id) {
        return userRepository
                .findById(id)
                .map(user -> DtoMapper
                        .builder()
                        .setUser(user)
                        .build());
    }

    @Override
    public UserDTO save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<Role> o = roleRepository.findByName("ROLE_USER");

        List<Role> roles = new ArrayList<>();
        if (o.isPresent()) {
            roles.add(o.orElseThrow());
        }
        user.setRoles(roles);

        return DtoMapper.builder().setUser(userRepository.save(user)).build();
    }

    @Override
    public Optional<UserDTO> update(UserRequest user, Long id) {
        Optional<User> userFind = userRepository.findById(id);
        User userOptional = null;
        if (userFind.isPresent()) {
            User userDb = userFind.orElseThrow();
            userDb.setUsername(user.getUsername());
            userDb.setEmail(user.getEmail());
            userOptional = userRepository.save(userDb);
        }
        return Optional.ofNullable(DtoMapper.builder()
                .setUser(userOptional)
                .build());
    }

    @Override
    public void remove(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        userRepository.delete(user);
    }
}