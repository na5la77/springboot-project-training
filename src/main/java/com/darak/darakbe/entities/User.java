package com.darak.darakbe.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone_number")
})
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name is required", groups = {Create.class})
    @Size(min = 3, message = "Name must be at least 3 characters long", groups = {Create.class})
    private String name;

    @NotEmpty(message = "Email is required", groups = {Create.class})
    @Email(message = "Email must be a valid email address", groups = {Create.class})
    @Column(unique = true)
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Please enter a valid phone number.")
    @Column(unique = true, nullable = true)
    private String phoneNumber;

    @Column(name = "email_verified")
    private boolean emailVerified = false;

    @Column(name = "phone_number_verified")
    private boolean phoneNumberVerified = false;

    @NotEmpty(message = "Password is required", groups = {Create.class})
    @Size(min = 8, message = "Password must be at least 8 characters long", groups = {Create.class})
    private String password;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "user_status")
    private String userStatus = "active";

    @ElementCollection
    @CollectionTable(name = "user_fcm_tokens", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "fcm_token")
    private List<String> fcmTokens;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin = false;

    // Validation groups
    public interface Create {}
    public interface Update {}
}
