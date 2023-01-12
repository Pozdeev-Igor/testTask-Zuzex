package com.example.testtask.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "houses")
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    private Long ownersId;

    @OneToMany(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<User> users;

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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
