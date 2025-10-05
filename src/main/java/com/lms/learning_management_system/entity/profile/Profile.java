package com.lms.learning_management_system.entity.profile;

import com.lms.learning_management_system.entity.user.User;
import com.lms.learning_management_system.utils.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "profile")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id" , nullable = false , foreignKey = @ForeignKey(name = "fk_profile"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_degree_id", foreignKey = @ForeignKey(name = "fk_education_degree"))
    private EducationDegree educationDegree;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "education_title_id" , foreignKey = @ForeignKey(name = "fk_education_title"))
    private EducationTitle educationTitle;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name" , length = 50)
    private String lastName;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 225)
    private String avatar;

    @Column(name = "date_of_birth" )
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Gender gender = Gender.MALE;


    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", updatable = false, insertable = false)
    private LocalDateTime updatedAt;



}
