package com.backend.usersapp.services;

import com.backend.usersapp.models.entities.User;
import com.backend.usersapp.models.request.UserRequest;
import com.backend.usersapp.models.response.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> findAll();
    Optional<UserDTO> findById(Long id);
    UserDTO save(User user);
    Optional<UserDTO> update(UserRequest user, Long id);
    void remove(Long id);
}