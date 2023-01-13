package com.example.testtask.components.service;

import com.example.testtask.components.DTO.HouseDTO;
import com.example.testtask.components.enums.ERole;
import com.example.testtask.components.models.House;
import com.example.testtask.components.models.Role;
import com.example.testtask.components.models.User;
import com.example.testtask.components.repos.HouseRepo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static com.example.testtask.components.enums.ERole.ROLE_OWNER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {HouseService.class})
@ExtendWith(SpringExtension.class)
class HouseServiceTest {
    @MockBean
    private HouseRepo houseRepo;

    @Autowired
    private HouseService houseService;

    @Test
    void shouldReturnAllHouses() {
        ArrayList<House> houseList = new ArrayList<>();
        House house = new House();
        houseList.add(house);
        houseRepo.save(house);
        when(houseRepo.findAll()).thenReturn(houseList);
        List<House> actualAll = houseService.getAll();
        assertSame(houseList, actualAll);
        assertFalse(actualAll.isEmpty());
        verify(houseRepo).findAll();
    }


    @Test
    void shouldReturnEmptyArrayListWhenTryingToGetAllHouses() {
        List<House> houses = new ArrayList<>();
        when(houseRepo.findAll()).thenReturn(houses);
        assertTrue(houseService.getAll().isEmpty());
    }

    @Test
    void ShouldReturnHouseById() {
        User user = new User();
        user.setAge(1);
        user.setName("Name");
        user.setPassword("password");

        House house = new House();
        house.setAddress("42 Main St");
        house.setOwnersId(user);
        when(houseRepo.getById(any())).thenReturn(house);

        assertSame(house, houseService.getById(123L, user));
        verify(houseRepo, atLeast(1)).getById(any());
    }

    @Test
    void testGetById2() throws NullPointerException {
        User user = new User();
        House house = new House();
       Optional<House> houseFromDB = Optional.of(house);
        house.setId(2L);
        user.setId(1L);
        when(houseRepo.getById(2L)).thenReturn(houseFromDB.orElseThrow());
        when(houseRepo.getById(2L).getOwnersId()).thenReturn(user.getId());

            assertNotEquals(houseFromDB.get().getOwnersId(), user.getId());
    }


    @Test
    void shouldSaveHouse() {

        User user = new User();
        House house = new House();
        house.setAddress("42 Main St");
        house.setOwnersId(user);
        house.setId(1L);

        when(houseRepo.save(house)).thenReturn(house);
        when(houseRepo.getById(1L)).thenReturn(house);

        assertEquals(house, houseRepo.getById(1L));
    }

    @Test
    @Disabled("doesn't work yet")
    void testSave2() {
        Role role = new Role();
        role.setName(ROLE_OWNER);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        User user = new User();
        user.setAge(1);
        user.setName("Name");
        user.setPassword("password");
        user.setRoles(roleSet);

        House house = new House();
        house.setId(4L);
        house.setAddress("42 Main St");
        house.setOwnersId(user);
        when(houseRepo.save(house)).thenReturn(house);

        HouseDTO houseDTO = new HouseDTO();
        houseDTO.setAddress("42 Main St");
        houseDTO.setId(123L);
        houseDTO.setOwnersId(123L);
        houseDTO.setUsers(new HashSet<>());


        assertEquals(ROLE_OWNER, user.getRoles().stream().findAny().get().getName());
        verify(houseRepo).save(house);
    }



    @Test
    void testModify() {

        User user = new User();
        user.setAge(1);
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setRoles(new HashSet<>());

        House house = new House();
        house.setAddress("42 Main St");
        house.setOwnersId(user);
        house.setUsers(new HashSet<>());
        when(houseRepo.getById((Long) any())).thenReturn(house);

        HouseDTO houseDTO = new HouseDTO();
        houseDTO.setAddress("42 Main St");
        houseDTO.setId(123L);
        houseDTO.setOwnersId(123L);
        houseDTO.setUsers(new HashSet<>());

        User user1 = new User();
        user1.setAge(1);
        user1.setName("Name");
        user1.setPassword("iloveyou");
        user1.setRoles(new HashSet<>());
        houseService.modify(houseDTO, user1);
    }


    @Test
    void testModify2() {
        User user = new User();
        user.setAge(1);
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setRoles(new HashSet<>());

        House house = new House();
        house.setAddress("42 Main St");
        house.setOwnersId(user);
        house.setUsers(new HashSet<>());

        User user1 = new User();
        user1.setAge(1);
        user1.setName("Name");
        user1.setPassword("iloveyou");
        user1.setRoles(new HashSet<>());

        House house1 = new House();
        house1.setAddress("42 Main St");
        house1.setOwnersId(user1);
        house1.setUsers(new HashSet<>());
        when(houseRepo.save((House) any())).thenReturn(house1);
        when(houseRepo.getById((Long) any())).thenReturn(house);

        HouseDTO houseDTO = new HouseDTO();
        houseDTO.setAddress("42 Main St");
        houseDTO.setId(123L);
        houseDTO.setOwnersId(123L);
        houseDTO.setUsers(new HashSet<>());

        Role role = new Role();
        role.setId(1);
        role.setName(ROLE_OWNER);

        HashSet<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        User user2 = new User();
        user2.setAge(1);
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setRoles(roleSet);
        assertSame(house1, houseService.modify(houseDTO, user2));
        verify(houseRepo).getById((Long) any());
        verify(houseRepo).save((House) any());
    }

    @Test
    void testDeleteById() {
        doNothing().when(houseRepo).deleteById(any());
        houseService.deleteById(123L);
        verify(houseRepo).deleteById(any());
    }

}

