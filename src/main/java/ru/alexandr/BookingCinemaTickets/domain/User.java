package ru.alexandr.BookingCinemaTickets.domain;

import jakarta.persistence.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserInfo userInfo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<RoleUser> roleUser = new HashSet<>();

    public User(String username,
                String password) {
        setUsername(username);
        setPasswordHash(password);
    }

    protected User() {

    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean setPasswordHash(String oldPassword, String newPassword) {
        if (!equalsPassword(oldPassword)) {
            return false;
        }

        setPasswordHash(newPassword);
        return true;
    }

    public boolean equalsPassword(String password) {
        return BCrypt.checkpw(password, getPasswordHash());
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        if (this.userInfo == userInfo) {
            return;
        }

        if (this.userInfo != null) {
            this.userInfo.setUser(null);
        }

        this.userInfo = userInfo;

        if (userInfo != null && userInfo.getUser() != this) {
            userInfo.setUser(this);
        }
    }

    public Set<RoleUser> getRoleUser() {
        return roleUser;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;

        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }

    private void setPasswordHash(String newPassword) {
        this.passwordHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
    }
}
