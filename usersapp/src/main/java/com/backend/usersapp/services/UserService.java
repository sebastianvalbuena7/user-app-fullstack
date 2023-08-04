package com.backend.usersapp.services;

import com.backend.usersapp.models.entities.User;
import com.backend.usersapp.models.request.UserRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    Optional<User> findById(Long id);
    User save(User user);
    Optional<User> update(UserRequest user, Long id);
    void remove(Long id);
}