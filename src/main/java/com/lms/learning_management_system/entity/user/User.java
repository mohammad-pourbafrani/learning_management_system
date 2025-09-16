package com.lms.learning_management_system.entity.user;


import com.lms.learning_management_system.utils.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "phone"),
                @UniqueConstraint(columnNames = "email")
        }
)
@Data // generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // no-args constructor
@AllArgsConstructor // all-args constructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // maps SERIAL in Postgres
    private Long id;

    @Column(length = 15, unique = true)
    private String phone;

    @Column(length = 100, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role = UserRole.STUDENT; // default 'student'

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    private boolean enable = false; // OTP verification

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", updatable = false, insertable = false)
    private LocalDateTime updatedAt;

}