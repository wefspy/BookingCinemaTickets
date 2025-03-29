package ru.alexandr.BookingCinemaTickets.domain;

import java.util.Objects;

public class User {
    private String username;
    private String password_hash;

    public User(String username,
                String password_hash) {
        this.username = username;
        this.password_hash = password_hash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;

        return Objects.equals(getUsername(), user.getUsername())
                && Objects.equals(getPassword_hash(), user.getPassword_hash());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getUsername(),
                getPassword_hash());
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password_hash='" + password_hash + '\'' +
                '}';
    }
}
