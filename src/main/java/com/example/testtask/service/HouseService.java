package com.example.testtask.service;

import com.example.testtask.models.House;
import com.example.testtask.repos.HouseRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseService {

    private final HouseRepo houseRepo;

    public HouseService(HouseRepo houseRepo) {
        this.houseRepo = houseRepo;
    }

    public List<House> getAll() {
        return houseRepo.findAll();
    }
}
