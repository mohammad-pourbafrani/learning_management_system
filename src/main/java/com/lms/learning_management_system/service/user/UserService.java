package com.lms.learning_management_system.service.user;

import com.lms.learning_management_system.dto.user.UserRegisterDto;
import com.lms.learning_management_system.entity.user.User;
import com.lms.learning_management_system.exception.user.UserEssentialArgumentException;
import com.lms.learning_management_system.exception.user.UserExistException;
import com.lms.learning_management_system.repository.user.UserLoginHistoryRepository;
import com.lms.learning_management_system.repository.user.UserRepository;
import com.lms.learning_management_system.repository.user.UserTokensRepository;
import com.lms.learning_management_system.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserLoginHistoryRepository userLoginHistoryRepository;
    private final UserTokensRepository userTokensRepository;

    public ApiResponse<Void> register(UserRegisterDto userRegisterDto) {
        if ((userRegisterDto.getEmail() == null || userRegisterDto.getEmail().isEmpty()) &&
                (userRegisterDto.getPhone() == null || userRegisterDto.getPhone().isEmpty())
        ) {
            throw new UserEssentialArgumentException("Either phone or email must be provided");
        }
        if (userRegisterDto.getEmail() != null && userRepository.existsByEmail(userRegisterDto.getEmail())) {
            throw new UserExistException("User with this Email already exist");
        }
        if (userRegisterDto.getPhone() != null && userRepository.existsByPhone(userRegisterDto.getPhone())) {
            throw new UserExistException("User with this Phone already exist");
        }

        User user = new User();
        user.setEmail(userRegisterDto.getEmail());
        user.setPhone(userRegisterDto.getPhone());
        //TODO: hash password
        user.setPassword(userRegisterDto.getPassword());
        user.setRole(userRegisterDto.getRole());
        userRepository.save(user);

        return new ApiResponse<Void>(HttpStatus.OK.value(), "user registered successfully");


    }

}
