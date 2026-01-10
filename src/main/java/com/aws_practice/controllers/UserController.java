package com.aws_practice.controllers;

import com.aws_practice.models.User;
import com.aws_practice.services.S3StorageService;
import com.aws_practice.services.user.IUserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public ResponseEntity<String> createUser(@RequestBody User user){
        userService.createUser(user);
        System.out.println(user.toString());
        return ResponseEntity.ok("Success!!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUser(@PathVariable long id){
        return ResponseEntity.ok(userService.findUserById(id).toString());
    }

    @PutMapping("/upload/{id}")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file, @PathVariable long id) throws IOException {
        String key = s3StorageService.uploadImage(file, "avatar");
        return ResponseEntity.ok("Success!!  " + key);
    }
}
