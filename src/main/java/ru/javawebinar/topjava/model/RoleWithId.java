package ru.javawebinar.topjava.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "user_roles")
@Access(AccessType.FIELD)
public class RoleWithId {

    @Id
    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "role")
    @NotBlank
    private String role;

    public RoleWithId() {}

    public RoleWithId(int userId, String role) {
        this.userId=userId;
        this.role=role;
    }

    public String getRole() {
        return role;
    }

    public int getUserId() {
        return userId;
    }
}
