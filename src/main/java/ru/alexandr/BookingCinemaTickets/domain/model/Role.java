package ru.alexandr.BookingCinemaTickets.domain.model;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rolesRoleIdSeq")
    @SequenceGenerator(name = "rolesRoleIdSeq", sequenceName = "roles_role_id_seq", allocationSize = 1)
    @Column(name = "role_id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<RoleUser> roleUser = new ArrayList<>();

    public Role(String name) {
        setName(name);
    }

    protected Role() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthority() {
        return String.format("ROLE_%s", name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RoleUser> getRoleUser() {
        return roleUser;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Role role = (Role) o;

        return Objects.equals(getId(), role.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
