package com.lms.learning_management_system.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_tokens",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "refresh_token"),
                @UniqueConstraint(columnNames = "access_token")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTokens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // SERIAL in Postgres

    //relation to user(FK user_id)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_token"))
    private User user;

    @Column(name = "refresh_token", nullable = false, unique = true , columnDefinition = "TEXT")
    private String refreshToken;

    @Column(name = "access_token" , nullable = false , unique = true , columnDefinition = "TEXT")
    private String accessToken;

    @Column(name = "created_at" , updatable = false , insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at"  , nullable = false)
    private LocalDateTime expiresAt;

    private Boolean revoked = false;

}
