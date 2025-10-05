package com.lms.learning_management_system.entity.profile;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "education_title" , uniqueConstraints = {
        @UniqueConstraint(columnNames = "title")
})
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EducationTitle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100 , nullable = false , unique = true )
    private String title;
}
