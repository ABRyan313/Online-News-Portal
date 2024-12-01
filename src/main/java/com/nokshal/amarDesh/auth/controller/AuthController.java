package com.nokshal.amarDesh.auth.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nokshal.amarDesh.auth.config.JwtConfig;
import com.nokshal.amarDesh.auth.model.AuthRequest;
import com.nokshal.amarDesh.auth.service.UserService;
import com.nokshal.amarDesh.auth.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            // Generate JWT token using authenticated username
            String jwtToken = jwtUtil.generateToken(authentication.getName());

            // Create response with JWT token
            Map<String, String> response = new HashMap<>();
            response.put("token", jwtConfig.getTokenPrefix() + " " + jwtToken);

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException ex) {
            // Handle incorrect credentials
            return ResponseEntity.status(401).body("Invalid username or password");
        } catch (Exception ex) {
            // Handle any other authentication error
            return ResponseEntity.status(500).body("Authentication failed");
        }
    }
}

