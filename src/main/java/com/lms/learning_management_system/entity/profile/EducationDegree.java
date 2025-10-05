package com.lms.learning_management_system.entity.profile;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "education_degree" , uniqueConstraints = {
        @UniqueConstraint(columnNames = "degree")
})
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EducationDegree {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100 , nullable = false , unique = true )
    private String degree;
}
