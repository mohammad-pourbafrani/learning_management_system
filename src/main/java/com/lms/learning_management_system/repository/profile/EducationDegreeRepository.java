package com.lms.learning_management_system.repository.profile;

import com.lms.learning_management_system.entity.profile.EducationDegree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationDegreeRepository extends JpaRepository<EducationDegree, Long> {
}
