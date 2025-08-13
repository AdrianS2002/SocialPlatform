package com.example.social_platform.persistance.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user", schema = "public")
public class UserEntity {

    @Id
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String bio;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

}
