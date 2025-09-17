package com.lms.learning_management_system.repository.user;

import com.lms.learning_management_system.entity.user.UserTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokensRepository extends JpaRepository<UserTokens,Long> {
    boolean existsByAccessToken(String accessToken);

    boolean existsByRefreshToken(String refreshToken);
}
