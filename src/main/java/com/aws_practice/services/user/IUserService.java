package com.aws_practice.services.user;

import com.aws_practice.dto.UserDto;
import com.aws_practice.models.User;
import com.aws_practice.request.CreateUserRequest;
import com.aws_practice.request.UserUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {

    User createUser(CreateUserRequest request);
    User findUserById(Long userId);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);
    UserDto convertUserToDto(User user);
    User getAuthenticatedUser();
    UserDto uploadAvatar(MultipartFile file, Long userId);

}
