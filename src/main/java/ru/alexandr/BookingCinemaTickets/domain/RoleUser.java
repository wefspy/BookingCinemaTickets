package ru.alexandr.BookingCinemaTickets.domain;

import java.util.Objects;

public class RoleUser {
    private Long id;
    private String username;
    private Long roleId;

    public RoleUser(Long id,
                    String username,
                    Long roleId) {
        this.id = id;
        this.username = username;
        this.roleId = roleId;
    }

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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleUser roleUser = (RoleUser) o;

        return Objects.equals(getId(), roleUser.getId())
                && Objects.equals(getUsername(), roleUser.getUsername())
                && Objects.equals(getRoleId(), roleUser.getRoleId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getUsername(),
                getRoleId());
    }

    @Override
    public String
    toString() {
        return "RoleUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", roleId=" + roleId +
                '}';
    }
}
