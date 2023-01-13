package com.example.testtask.components.service;

import com.example.testtask.components.DTO.UserDTO;
import com.example.testtask.components.models.User;
import com.example.testtask.components.repos.UsersRepo;
import com.example.testtask.components.utils.CustomPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @MockBean
    private CustomPasswordEncoder customPasswordEncoder;

    @Autowired
    private UserService userService;

    @MockBean
    private UsersRepo usersRepo;


    @BeforeEach
    void setUp() {
        usersRepo.deleteAll();
    }

    @Test
    @Disabled
    void shouldSaveUserToDB() {

        User user = new User();
        user.setName("Name");
        user.setAge(1);
        user.setPassword("password");
        when(usersRepo.save(user)).thenReturn(user);
        verify(usersRepo).save(user);
    }




    @Test
    void shouldDeleteUserById() {
        User user = new User();
        user.setName("Name");
        user.setPassword("password");
        Optional<User> ofResult = Optional.of(user);
        doNothing().when(usersRepo).deleteById(1L);
        when(usersRepo.findById(1L)).thenReturn(ofResult);
        userService.deleteUser(1L);
        verify(usersRepo).findById(1L);
        verify(usersRepo).deleteById(1L);

        assertEquals(user, usersRepo.findById(1L).get());
    }



    @Test
    @Disabled("Пока не работает")
    void shouldUpdateUserFromDB() {
        User user = new User();
        user.setAge(1);
        user.setName("Name");
        user.setPassword("password");
        Optional<User> ofResult = Optional.of(user);
        when(usersRepo.findById(any())).thenReturn(ofResult);

        UserDTO userDTO = new UserDTO();
        userDTO.setAge(2);
        userDTO.setId(123L);
        userDTO.setName("Name 2");
        userDTO.setPassword("password2");

        User user1 = new User();
        user1.setAge(3);
        user1.setName("Name 3");
        user1.setPassword("password3");
        userService.updateUser(userDTO, user1);
        verify(usersRepo).findById(any());
        verify(usersRepo).save(user);

        assertEquals(2, user.getAge());
        assertEquals("password2", user.getPassword());
        assertEquals("Name 2", user.getName());

        assertEquals(2, userDTO.getAge());
        assertEquals("password2", userDTO.getPassword());
        assertEquals("Name 2", userDTO.getName());
        assertEquals(123L, userDTO.getId().longValue());
        assertEquals(3, user1.getAge());
        assertEquals("password3", user1.getPassword());
        assertEquals("Name 3", user1.getName());
    }


    @Test
    void testFindAll() {
        ArrayList<User> userList = new ArrayList<>();
        userList.add(new User());
        when(usersRepo.findAll()).thenReturn(userList);
        List<User> actualFindAllResult = userService.findAll();
        assertSame(userList, actualFindAllResult);
        assertFalse(actualFindAllResult.isEmpty());
        verify(usersRepo).findAll();
    }

    @Test
    void testFindById() {
        User user = new User();
        user.setAge(1);
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setRoles(new HashSet<>());
        Optional<User> ofResult = Optional.of(user);

        User user1 = new User();
        user1.setAge(1);
        user1.setName("Name");
        user1.setPassword("iloveyou");
        user1.setRoles(new HashSet<>());
        when(usersRepo.getById(any())).thenReturn(user1);
        when(usersRepo.findById(any())).thenReturn(ofResult);
        assertSame(user1, userService.findById(123L));
        verify(usersRepo).getById(any());
        verify(usersRepo).findById(any());
    }

    @Test
    void testFindById2() {
        User user = new User();
        user.setAge(1);
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setRoles(new HashSet<>());
        when(usersRepo.getById(any())).thenReturn(user);
        when(usersRepo.findById(any())).thenReturn(Optional.empty());
        assertNull(userService.findById(123L));
        verify(usersRepo).findById(any());
    }
}

