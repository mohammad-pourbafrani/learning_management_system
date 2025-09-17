package com.lms.learning_management_system.repository.user;

import com.lms.learning_management_system.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByPhone(String phone);
}
