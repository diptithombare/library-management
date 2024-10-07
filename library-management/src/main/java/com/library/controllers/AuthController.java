// src/main/java/com/library/controllers/AuthController.java
package com.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.config.JwtUtil;
import com.library.models.User;
import com.library.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // DTOs for request payloads
    static class SignupRequest {
        public String username;
        public String password;
        public String role; // LIBRARIAN or MEMBER
    }

    static class LoginRequest {
        public String username;
        public String password;
    }

    static class JwtResponse {
        public String token;

        public JwtResponse(String token){
            this.token = token;
        }

        // Getter
        public String getToken() {
            return token;
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Validated @RequestBody SignupRequest signupRequest){
        if(userService.findByUsername(signupRequest.username).isPresent()){
            return ResponseEntity
                    .badRequest()
                    .body("Error: Username is already taken!");
        }

        if(!signupRequest.role.equals("LIBRARIAN") && !signupRequest.role.equals("MEMBER")){
            return ResponseEntity
                    .badRequest()
                    .body("Error: Role must be LIBRARIAN or MEMBER");
        }

        // Create new user account
        User user = new User();
        user.setUsername(signupRequest.username);
        user.setPassword(passwordEncoder.encode(signupRequest.password));
        user.setRole(signupRequest.role);
        user.setActive(true);

        userService.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginRequest loginRequest){
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.username, 
                    loginRequest.password
                )
            );

            String jwt = jwtUtil.generateToken(loginRequest.username);
            return ResponseEntity.ok(new JwtResponse(jwt));

        } catch (BadCredentialsException e){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Error: Invalid username or password");
        }
    }
}
