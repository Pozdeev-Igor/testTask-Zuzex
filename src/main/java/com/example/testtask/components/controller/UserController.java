package com.example.testtask.components.controller;

import com.example.testtask.components.DTO.UserDTO;
import com.example.testtask.components.models.User;
import com.example.testtask.components.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RequestMapping("/api/users")
@RestController
public class UserController {

private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/new")
    public ResponseEntity<User> addNewUser (@RequestBody UserDTO userDTO) {
        User newUser = new User();
        userService.save(newUser, userDTO);
        return ok(newUser);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO, @AuthenticationPrincipal User user) {
        userService.updateUser(userDTO, user);
        return ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ok(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
          List<User> allUsers = userService.findAll();
        return ok(allUsers);
    }

    @GetMapping("{/id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
            User user = userService.findById(id);
        return ok(user);
    }
}
