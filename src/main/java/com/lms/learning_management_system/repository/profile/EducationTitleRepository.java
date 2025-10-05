package com.lms.learning_management_system.repository.profile;

import com.lms.learning_management_system.entity.profile.EducationTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationTitleRepository extends JpaRepository<EducationTitle, Long> {
}
