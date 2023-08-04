package com.backend.usersapp.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import javax.lang.model.element.Name;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
//    @Min() @Max()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre de usuario es necesario")
    @Size(min = 4, max = 10, message = "Debes escribir un username de entre 4 y 10 caracteres")
    @Column(unique = true)
    private String username;

    @NotEmpty(message = "Deberias agregar una contraseña")
    private String password;

    @Email(message = "Debes colocar un email valido")
    @NotEmpty()
    @Column(unique = true)
    private String email;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"})}
    )
    private List<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}