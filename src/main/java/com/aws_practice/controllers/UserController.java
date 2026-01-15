package com.aws_practice.controllers;

import com.aws_practice.dto.UserDto;
import com.aws_practice.models.User;
import com.aws_practice.request.CreateUserRequest;
import com.aws_practice.request.UserUpdateRequest;
import com.aws_practice.response.ApiResponse;
import com.aws_practice.services.S3StorageService;
import com.aws_practice.services.user.IUserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    private final S3StorageService s3StorageService;

    @GetMapping("/hello")
    public String SayHello(){
        return "Hello World!!";
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request){
        User user = userService.createUser(request);
        UserDto userDto = userService.convertUserToDto(user);
        return ResponseEntity.ok(new ApiResponse("Create User Success!", userDto));
    }

    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId){
        User user = userService.findUserById(userId);
        UserDto userDto = userService.convertUserToDto(user);
        return ResponseEntity.ok(new ApiResponse("Success", userDto));
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request, @PathVariable Long userId) {
        User user = userService.updateUser(request, userId);
        UserDto userDto = userService.convertUserToDto(user);
        return ResponseEntity.ok(new ApiResponse("Update User Success!", userDto));
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse("Delete User Success!", null));
    }

    @PutMapping("/upload/{id}")
    public ResponseEntity<ApiResponse> uploadAvatar(@RequestParam("file") MultipartFile file, @PathVariable Long id) throws IOException {
        UserDto userDto = userService.uploadAvatar(file, id);
        return ResponseEntity.ok(new ApiResponse("Upload Avatar Success!", userDto));
    }

}
