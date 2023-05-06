package com.example.miniproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;
    @Column(nullable = true)
    private String password;
    @Column(nullable = true)
    private String nickname;
    @Column(nullable = true)
    private String name;

    public User(String userId){
        this.userId = userId;
    }

}
