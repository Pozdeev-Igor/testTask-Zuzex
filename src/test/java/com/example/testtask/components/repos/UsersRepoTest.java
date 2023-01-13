package com.example.testtask.components.repos;

import com.example.testtask.components.models.User;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {UsersRepo.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.example.testtask.components.models"})
@DataJpaTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
class UsersRepoTest {
    @Autowired
    private UsersRepo usersRepo;


    @Test
    void shouldCheckIfExistsByName() {
        User user = new User();
        user.setName("Name");
        user.setPassword("password");

        User user1 = new User();
        user1.setName("Second Name");
        user1.setPassword("password");
        usersRepo.save(user);
        usersRepo.save(user1);
        assertFalse(usersRepo.existsByName("foo"));
        assertTrue(usersRepo.existsByName("Name"));
        assertNotEquals(usersRepo.findByName("Name"), usersRepo.findByName("Second Name"));
    }

    @Test
    void testFindByName() {
        User user = new User();
        user.setName("Name");
        user.setPassword("password");

        User user1 = new User();
        user1.setName("Second Name");
        user1.setPassword("password");
        usersRepo.save(user);
        usersRepo.save(user1);
        assertFalse(usersRepo.findByName("foo").isPresent());
        assertTrue(usersRepo.findByName("Name").isPresent());
        assertTrue(usersRepo.findByName("Second Name").isPresent());

    }
}

