package ru.alexandr.BookingCinemaTickets.domain.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "roles_users")
public class RoleUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rolesUsersRoleUserIdSeq")
    @SequenceGenerator(
            name = "rolesUsersRoleUserIdSeq",
            sequenceName = "roles_users_role_user_id_seq",
            allocationSize = 1)
    @Column(name = "role_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public RoleUser(User user,
                    Role role) {
        setUser(user);
        setRole(role);
    }

    protected RoleUser() {

    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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
