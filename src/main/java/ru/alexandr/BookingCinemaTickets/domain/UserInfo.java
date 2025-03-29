package ru.alexandr.BookingCinemaTickets.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class UserInfo {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private LocalDateTime createdAt;

    public UserInfo(Long id,
                    String username,
                    String email,
                    String phoneNumber,
                    LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInfo userInfo = (UserInfo) o;

        return Objects.equals(getId(), userInfo.getId())
                && Objects.equals(getUsername(), userInfo.getUsername())
                && Objects.equals(getEmail(), userInfo.getEmail())
                && Objects.equals(getPhoneNumber(), userInfo.getPhoneNumber())
                && Objects.equals(getCreatedAt(), userInfo.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getUsername(),
                getEmail(),
                getPhoneNumber(),
                getCreatedAt());
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
