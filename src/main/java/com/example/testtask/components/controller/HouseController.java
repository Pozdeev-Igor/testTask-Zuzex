package com.example.testtask.components.controller;

import com.example.testtask.components.DTO.HouseDTO;
import com.example.testtask.components.models.House;
import com.example.testtask.components.models.User;
import com.example.testtask.components.service.HouseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;


@RequestMapping("/api/houses")
@RestController
public class HouseController {

    private final HouseService houseService;

    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }

    @GetMapping
    public ResponseEntity<List<House>> getAllHouses() {
                List<House> houses = houseService.getAll();
        return ResponseEntity.ok(houses);
    }

    @GetMapping("/{houseId}")
    public ResponseEntity<?> getHouseById(@PathVariable Long houseId, @AuthenticationPrincipal User user) {
        try {
        House house = houseService.getById(houseId, user);
        return ResponseEntity.ok(house);
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Дома с таким id не существует!");
        }
    }


    @GetMapping("/{houseId}/users")
    public ResponseEntity<Set<User>> getRoomersOfCurrentHouse(@PathVariable Long houseId, @AuthenticationPrincipal User user) {
        Set<User> roomers = houseService.getById(houseId, user).getUsers();
        return ResponseEntity.ok(roomers);
    }

    @PostMapping("/new")
    public ResponseEntity<?> addNewHouse(@RequestBody HouseDTO houseDTO, @AuthenticationPrincipal User user) {
        House house = houseService.save(houseDTO, user);
        return ResponseEntity.ok(house);
    }

    @PutMapping("/{houseId}")
    public ResponseEntity<?> modifyHouse(@RequestBody HouseDTO houseDTO, @AuthenticationPrincipal User user) {
        House modifiedHouse = houseService.modify(houseDTO, user);
        return ResponseEntity.ok(modifiedHouse);
    }

    @DeleteMapping("/{houseId}")
    public ResponseEntity<?> deleteHouse(@PathVariable Long houseId, @AuthenticationPrincipal User user) {
        try {
            houseService.deleteById(houseId);
            return ResponseEntity.ok(houseId);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,  "Ошибка! На ключ (id)=(" + houseId + ") всё ещё есть ссылки в таблице");
        }
    }
}
