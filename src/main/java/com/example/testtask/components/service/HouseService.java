package com.example.testtask.components.service;

import com.example.testtask.components.DTO.HouseDTO;
import com.example.testtask.components.models.House;
import com.example.testtask.components.models.User;
import com.example.testtask.components.repos.HouseRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.example.testtask.components.enums.ERole.ROLE_OWNER;

@Service
public class HouseService {

    private final HouseRepo houseRepo;

    public HouseService(HouseRepo houseRepo) {
        this.houseRepo = houseRepo;
    }

    public List<House> getAll() {
        return houseRepo.findAll();
    }

    public House getById(Long houseId, User user) throws NullPointerException{
        House houseFromDB = houseRepo.getById(houseId);
        if (Objects.equals(houseFromDB.getOwnersId(), user.getId())) {
        return houseRepo.getById(houseId);
        }
        return null;
    }


    public House save(HouseDTO houseDTO, User user) {
            House newHouse = new House();
        if (user.getRoles().stream().findAny().get().equals(ROLE_OWNER)) {
            newHouse.setAddress(houseDTO.getAddress());
            newHouse.setUsers(houseDTO.getUsers());
            newHouse.setOwnersId(user);
        }
           return houseRepo.save(newHouse);
    }

    public House modify(HouseDTO houseDTO, User user) {
        House house = houseRepo.getById(houseDTO.getId());
        if (user.getRoles().stream().findAny().get().equals(ROLE_OWNER)) {
            house.setAddress(houseDTO.getAddress());
            house.setUsers(houseDTO.getUsers());
        }
        return houseRepo.save(house);
    }

    public void deleteById(Long houseId) {
        houseRepo.deleteById(houseId);
    }
}
