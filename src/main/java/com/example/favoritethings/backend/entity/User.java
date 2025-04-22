// src/main/java/com/example/favoritethings/backend/entity/User.java
package com.example.favoritethings.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private LocalDate birthDate;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private boolean emailConfirmed;

    /**
     * Один пользователь — одна роль.
     * При удалении роли в БД: role_id → NULL (ON DELETE SET NULL).
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(
      name = "role_id",
      foreignKey = @ForeignKey(
        name = "fk_users_role",
        foreignKeyDefinition = "FOREIGN KEY(role_id) REFERENCES roles(id) ON DELETE SET NULL"
      )
    )
    private Role role;

    /**
     * Связанные опросы: удалятся вместе с пользователем.
     */
    @OneToMany(
      mappedBy = "user",
      cascade = CascadeType.ALL,
      orphanRemoval = true
    )
    private Set<Survey> surveys = new HashSet<>();

    // геттеры / сеттеры

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isEmailConfirmed() { return emailConfirmed; }
    public void setEmailConfirmed(boolean emailConfirmed) { this.emailConfirmed = emailConfirmed; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Set<Survey> getSurveys() { return surveys; }
    public void setSurveys(Set<Survey> surveys) { this.surveys = surveys; }
}
