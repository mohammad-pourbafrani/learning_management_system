package com.lms.learning_management_system.repository.user;

import com.lms.learning_management_system.entity.user.UserLoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginHistoryRepository extends JpaRepository<UserLoginHistory,Long> {
}
