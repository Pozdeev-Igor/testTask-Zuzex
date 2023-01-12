package com.example.testtask.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class House {
    @Id
    private Long id;
    private String address;
    private Long ownersId;

    public Long getId() {
        return id;
    }

//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getOwnersId() {
        return ownersId;
    }

    public void setOwnersId(User user) {
        this.ownersId = user.getId();
    }
}
