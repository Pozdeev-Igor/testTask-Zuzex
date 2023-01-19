package com.example.testtask.components.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.example.testtask.components.DTO.AuthCredentialsRequest;
import com.example.testtask.components.DTO.UserDTO;
import com.example.testtask.components.repos.UsersRepo;
import com.example.testtask.components.service.UserService;
import com.example.testtask.components.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AuthController.class})
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    @Autowired
    private AuthController authController;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserService userService;

    @MockBean
    private UsersRepo usersRepo;

    @Test
    void testLogin() throws Exception {
        when(jwtUtil.generateToken(any())).thenReturn("ABC123");
        when(authenticationManager.authenticate(any()))
                .thenReturn(new TestingAuthenticationToken(new User("janedoe", "iloveyou", new ArrayList<>()), "Credentials"));

        AuthCredentialsRequest authCredentialsRequest = new AuthCredentialsRequest();
        authCredentialsRequest.setPassword("iloveyou");
        authCredentialsRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(authCredentialsRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"password\":\"iloveyou\",\"username\":\"janedoe\",\"authorities\":[],\"accountNonExpired\":true,\"accountNonLocked"
                                        + "\":true,\"credentialsNonExpired\":true,\"enabled\":true}"));
    }

    @Test
    void testLogin2() throws Exception {
        when(jwtUtil.generateToken(any())).thenThrow(new BadCredentialsException("?"));
        when(authenticationManager.authenticate(any())).thenReturn(
                new TestingAuthenticationToken(new User("janedoe", "iloveyou", new ArrayList<>()), "Credentials"));

        AuthCredentialsRequest authCredentialsRequest = new AuthCredentialsRequest();
        authCredentialsRequest.setPassword("iloveyou");
        authCredentialsRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(authCredentialsRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(401));
    }

    @Test
    void testRegistration() throws Exception {
        when(usersRepo.existsByName(any())).thenReturn(true);

        UserDTO userDTO = new UserDTO();
        userDTO.setAge(1);
        userDTO.setId(123L);
        userDTO.setName("Name");
        userDTO.setPassword("iloveyou");
        userDTO.setRoles(new HashSet<>());
        String content = (new ObjectMapper()).writeValueAsString(userDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void testRegistration2() throws Exception {
        when(usersRepo.existsByName(any())).thenReturn(false);

        UserDTO userDTO = new UserDTO();
        userDTO.setAge(1);
        userDTO.setId(123L);
        userDTO.setName("Name");
        userDTO.setPassword("iloveyou");
        userDTO.setRoles(new HashSet<>());
        String content = (new ObjectMapper()).writeValueAsString(userDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(417));
    }

    @Test
    void testValidateToken() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/auth/validate")
                .param("token", "");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}

