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
    @Column(name = "username")
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserInfo userInfo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<RoleUser> roleUser = new HashSet<>();

    public User(String username,
                String password) {
        this.username = username;
        setPassword(password);
    }

    protected User() {

    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean setPassword(String oldPassword, String newPassword) {
        if (!equalsPassword(oldPassword)) {
            return false;
        }

        setPassword(newPassword);
        return true;
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

        return Objects.equals(getUsername(), user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }

    private void setPassword(String newPassword) {
        this.passwordHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
    }

    private boolean equalsPassword(String password) {
        return BCrypt.checkpw(password, getPasswordHash());
    }
}
