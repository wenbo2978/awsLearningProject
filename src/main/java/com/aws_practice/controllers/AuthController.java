package com.aws_practice.controllers;

import com.aws_practice.request.LoginRequest;
import com.aws_practice.response.ApiResponse;
import com.aws_practice.response.JwtResponse;
import com.aws_practice.security.jwt.JwtUtils;
import com.aws_practice.security.user.ShopUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request){
        System.out.println("login controller!!");
        System.out.println(request.toString());
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateTokenForUser(authentication);
        ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
        System.out.println("userDetails");
        System.out.println(userDetails.toString());
        JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);
        return ResponseEntity.ok(new ApiResponse("success", jwtResponse));
    }

}
