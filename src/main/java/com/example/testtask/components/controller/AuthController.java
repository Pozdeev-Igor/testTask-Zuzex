package com.example.testtask.components.controller;

import com.example.testtask.components.DTO.AuthCredentialsRequest;
import com.example.testtask.components.DTO.UserDTO;
import com.example.testtask.components.repos.UsersRepo;
import com.example.testtask.components.models.User;
import com.example.testtask.components.service.UserService;
import com.example.testtask.components.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;



/**
 * Этот класс я не писал, забрал его из моего домашнего проекта.
 */

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsersRepo usersRepo;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UsersRepo usersRepo, UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.usersRepo = usersRepo;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserDTO userDTO) {

        if (usersRepo.existsByName(userDTO.getName())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        User user = new User();

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }

        userService.save(user, userDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthCredentialsRequest request) {
        try {
            Authentication authenticate = authenticationManager
            .authenticate(
                    new UsernamePasswordAuthenticationToken(
                    request.getUsername(), request.getPassword()
                    )
            );

            UserDetails user = (UserDetails) authenticate.getPrincipal();

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            jwtUtil.generateToken(user)
                    )
                    .body(user);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token, @AuthenticationPrincipal User user) {
        if (token.equals("") || token == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            try {
                Boolean isValidToken = jwtUtil.validateToken(token, (UserDetails) user);
                return ResponseEntity.ok(isValidToken);
            } catch (ExpiredJwtException exception) {
                return ResponseEntity.ok(false);
            }
        }
    }
}
