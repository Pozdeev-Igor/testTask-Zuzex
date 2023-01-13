package com.example.testtask.components.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class CustomPasswordEncoderTest {

    @Test
    void testConstructor() {
        assertTrue((new CustomPasswordEncoder()).getPasswordEncoder() instanceof BCryptPasswordEncoder);
    }
}

