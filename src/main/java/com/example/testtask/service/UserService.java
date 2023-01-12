package com.example.testtask.service;

import com.example.testtask.DTO.UserDTO;
import com.example.testtask.models.User;
import com.example.testtask.repos.UsersRepo;
import com.example.testtask.utils.CustomPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UsersRepo usersRepo;
    private CustomPasswordEncoder passwordEncoder;

    public UserService(UsersRepo usersRepo, CustomPasswordEncoder passwordEncoder) {
        this.usersRepo = usersRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(User user, UserDTO userDTO) {
        user.setAge(userDTO.getAge());
        user.setName(userDTO.getName());
        user.setPassword(passwordEncoder.getPasswordEncoder().encode(userDTO.getPassword()));
        usersRepo.save(user);
    }
}
