package com.example.testtask.components.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.example.testtask.components.DTO.HouseDTO;
import com.example.testtask.components.models.House;
import com.example.testtask.components.models.User;
import com.example.testtask.components.service.HouseService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

@ContextConfiguration(classes = {HouseController.class})
@ExtendWith(SpringExtension.class)
class HouseControllerTest {
    @Autowired
    private HouseController houseController;

    @MockBean
    private HouseService houseService;


    @Test
    void testAddNewHouse() throws Exception {
        User user = new User();
        user.setAge(1);
        user.setId(123L);
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setRoles(new HashSet<>());

        House house = new House();
        house.setAddress("42 Main St");
        house.setId(123L);
        house.setOwnersId(user);
        house.setUsers(new HashSet<>());
        when(houseService.save((HouseDTO) any(), (User) any())).thenReturn(house);

        HouseDTO houseDTO = new HouseDTO();
        houseDTO.setAddress("42 Main St");
        houseDTO.setId(123L);
        houseDTO.setOwnersId(123L);
        houseDTO.setUsers(new HashSet<>());
        String content = (new ObjectMapper()).writeValueAsString(houseDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/houses/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(houseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":123,\"address\":\"42 Main St\",\"ownersId\":123,\"users\":[]}"));
    }

    @Test
    void testAddNewHouse2() throws Exception {
        when(houseService.save((HouseDTO) any(), (User) any()))
                .thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));

        HouseDTO houseDTO = new HouseDTO();
        houseDTO.setAddress("42 Main St");
        houseDTO.setId(123L);
        houseDTO.setOwnersId(123L);
        houseDTO.setUsers(new HashSet<>());
        String content = (new ObjectMapper()).writeValueAsString(houseDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/houses/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(houseController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }


    @Test
    void testDeleteHouse() throws Exception {
        doNothing().when(houseService).deleteById((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/houses/{houseId}", 123L);
        MockMvcBuilders.standaloneSetup(houseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("123"));
    }


    @Test
    void testDeleteHouse2() throws Exception {
        doThrow(new NullPointerException()).when(houseService).deleteById((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/houses/{houseId}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(houseController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500));
    }


    @Test
    void testDeleteHouse3() throws Exception {
        doNothing().when(houseService).deleteById((Long) any());
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders.delete("/api/houses/{houseId}", 123L);
        deleteResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(houseController)
                .build()
                .perform(deleteResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("123"));
    }


    @Test
    void testGetAllHouses() throws Exception {
        when(houseService.getAll()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/houses");
        MockMvcBuilders.standaloneSetup(houseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testGetAllHouses2() throws Exception {
        when(houseService.getAll()).thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/houses");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(houseController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    @Test
    void testGetHouseById() throws Exception {
        User user = new User();
        user.setAge(1);
        user.setId(123L);
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setRoles(new HashSet<>());

        House house = new House();
        house.setAddress("42 Main St");
        house.setId(123L);
        house.setOwnersId(user);
        house.setUsers(new HashSet<>());
        when(houseService.getById((Long) any(), (User) any())).thenReturn(house);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/houses/{houseId}", 123L);
        MockMvcBuilders.standaloneSetup(houseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":123,\"address\":\"42 Main St\",\"ownersId\":123,\"users\":[]}"));
    }

    @Test
    void testGetHouseById2() throws Exception {
        when(houseService.getById((Long) any(), (User) any())).thenThrow(new NullPointerException());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/houses/{houseId}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(houseController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500));
    }


    @Test
    void testGetHouseById3() throws Exception {
        when(houseService.getById((Long) any(), (User) any()))
                .thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/houses/{houseId}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(houseController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }


    @Test
    void testGetRoomersOfCurrentHouse() throws Exception {
        User user = new User();
        user.setAge(1);
        user.setId(123L);
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setRoles(new HashSet<>());

        House house = new House();
        house.setAddress("42 Main St");
        house.setId(123L);
        house.setOwnersId(user);
        house.setUsers(new HashSet<>());
        when(houseService.getById((Long) any(), (User) any())).thenReturn(house);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/houses/{houseId}/users", 123L);
        MockMvcBuilders.standaloneSetup(houseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    @Test
    void testGetRoomersOfCurrentHouse2() throws Exception {
        when(houseService.getById((Long) any(), (User) any()))
                .thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/houses/{houseId}/users", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(houseController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }


    @Test
    void testModifyHouse() throws Exception {
        User user = new User();
        user.setAge(1);
        user.setId(123L);
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setRoles(new HashSet<>());

        House house = new House();
        house.setAddress("42 Main St");
        house.setId(123L);
        house.setOwnersId(user);
        house.setUsers(new HashSet<>());
        when(houseService.modify((HouseDTO) any(), (User) any())).thenReturn(house);

        HouseDTO houseDTO = new HouseDTO();
        houseDTO.setAddress("42 Main St");
        houseDTO.setId(123L);
        houseDTO.setOwnersId(123L);
        houseDTO.setUsers(new HashSet<>());
        String content = (new ObjectMapper()).writeValueAsString(houseDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/houses/{houseId}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(houseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":123,\"address\":\"42 Main St\",\"ownersId\":123,\"users\":[]}"));
    }

    @Test
    void testModifyHouse2() throws Exception {
        when(houseService.modify((HouseDTO) any(), (User) any()))
                .thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));

        HouseDTO houseDTO = new HouseDTO();
        houseDTO.setAddress("42 Main St");
        houseDTO.setId(123L);
        houseDTO.setOwnersId(123L);
        houseDTO.setUsers(new HashSet<>());
        String content = (new ObjectMapper()).writeValueAsString(houseDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/houses/{houseId}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(houseController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }
}

