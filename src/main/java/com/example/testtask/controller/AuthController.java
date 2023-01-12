package com.example.testtask.controller;

import com.example.testtask.DTO.UserDTO;
import com.example.testtask.models.User;
import com.example.testtask.repos.UsersRepo;
import com.example.testtask.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UsersRepo usersRepo;
    private UserService userService;


    public AuthController(UsersRepo usersRepo, UserService userService) {
        this.usersRepo = usersRepo;
        this.userService = userService;
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
}
