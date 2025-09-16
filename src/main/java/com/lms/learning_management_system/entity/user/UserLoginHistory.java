package com.lms.learning_management_system.entity.user;

import com.lms.learning_management_system.utils.LoginStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "login_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // SERIAL in Postgres

    //relation to User (FK user_id)
    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "user_id" ,  nullable = false , foreignKey = @ForeignKey(name = "fk_user"))
    private  User user;

    @Column(name = "login_time" , updatable = false , insertable = false) // handled by DB default
    private LocalDateTime loginTime;

    @Column(name = "ip_address" , nullable = false , length = 45)
    private String ipAddress;

    @Column(name = "user_agent" , columnDefinition = "TEXT")
    private  String userAgent;

    @Enumerated(EnumType.STRING)
    @Column(name = "status" , nullable = false , length = 20)
    private LoginStatus loginStatus = LoginStatus.SUCCESS; // default = 'success'

}
