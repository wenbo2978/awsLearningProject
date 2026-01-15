package com.aws_practice.services.user;

import com.aws_practice.dto.UserDto;
import com.aws_practice.exceptions.AlreadyExistsException;
import com.aws_practice.exceptions.ResourceNotFoundException;
import com.aws_practice.models.User;
import com.aws_practice.repositories.UserRepository;
import com.aws_practice.request.CreateUserRequest;
import com.aws_practice.request.UserUpdateRequest;
import com.aws_practice.services.S3StorageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final S3StorageService s3StorageService;

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    return userRepository.save(user);
                }).orElseThrow(() -> new AlreadyExistsException("Oops!" +request.getEmail() +" already exists!"));
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found!!"));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return  userRepository.findById(userId).map(existingUser ->{
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            existingUser.setPhone(request.getPhone());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found!"));

    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository :: delete, () ->{
            throw new ResourceNotFoundException("User not found!");
        });
    }

    @Override
    public UserDto convertUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDto uploadAvatar(MultipartFile file, Long userId){
        try{
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User Not Found!!"));
            String key = user.getAvatar();
            if(key.isBlank()){
                key = s3StorageService.uploadImage(file, "avatar");
            }else{
                key = s3StorageService.updateImage(file, key);
            }
            user.setAvatar(key);
            return modelMapper.map(userRepository.save(user), UserDto.class);
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
