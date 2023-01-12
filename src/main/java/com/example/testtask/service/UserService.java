package com.example.testtask.service;

import com.example.testtask.DTO.UserDTO;
import com.example.testtask.models.User;
import com.example.testtask.repos.UsersRepo;
import com.example.testtask.utils.CustomPasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UsersRepo usersRepo;
    private final CustomPasswordEncoder passwordEncoder;

    public UserService(UsersRepo usersRepo, CustomPasswordEncoder passwordEncoder) {
        this.usersRepo = usersRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(User user, UserDTO userDTO) {
        user.setAge(userDTO.getAge());
        user.setName(userDTO.getName());
        user.setPassword(passwordEncoder.getPasswordEncoder().encode(userDTO.getPassword()));
        user.setRoles(userDTO.getRoles());
        usersRepo.save(user);
    }

    public void deleteUser(Long userId) {
        if (usersRepo.findById(userId).isPresent()) {
            usersRepo.deleteById(userId);
        }
    }

    public void updateUser(UserDTO userDTO, Long id) {
        if (usersRepo.findById(id).isPresent()) {
            User userFromDB = usersRepo.getById(id);
            userFromDB.setAge(userDTO.getAge());
            userFromDB.setName(userDTO.getName());
            usersRepo.save(userFromDB);
        }
    }

    public List<User> findAll() {
        return usersRepo.findAll();
    }

    public User findById(Long id) {
        User fromDB = null;
        if (usersRepo.findById(id).isPresent()) {
                fromDB = usersRepo.getById(id);
        }
        return fromDB;
    }
}
