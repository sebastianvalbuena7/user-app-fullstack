package com.backend.usersapp.models.response.mapper;

import com.backend.usersapp.models.entities.User;
import com.backend.usersapp.models.response.UserDTO;

public class DtoMapper {
    private User user;

    private DtoMapper() {
    }

    public static DtoMapper builder() {
        return new DtoMapper();
    }

    public DtoMapper setUser(User user) {
        this.user = user;
        return this;
    }

    public UserDTO build() {
        if (user == null) throw new RuntimeException("Debe pasar el entity user");
        return new UserDTO(this.user.getId(), this.user.getUsername(), this.user.getEmail());
    }
}