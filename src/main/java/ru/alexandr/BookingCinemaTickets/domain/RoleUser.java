package ru.alexandr.BookingCinemaTickets.domain;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "roles_users")
public class RoleUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "roles_users_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public RoleUser(User user,
                    Role role) {
        this.user = user;
        this.role = role;
    }

    protected RoleUser() {

    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (this.user != null) {
            this.user.getRoles().remove(this);
        }

        this.user = user;

        if (user != null) {
            user.getRoles().add(this);
        }
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        if (this.role != null) {
            this.role.getUsers().remove(this);
        }

        this.role = role;

        if (role != null) {
            role.getUsers().add(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleUser roleUser = (RoleUser) o;

        return Objects.equals(getId(), roleUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String
    toString() {
        return "RoleUser{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", role=" + role +
                '}';
    }
}
