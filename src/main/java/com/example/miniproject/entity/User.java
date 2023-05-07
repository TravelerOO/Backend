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

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String name;

    public User(String userId, String password, String nickname, String name) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
    }
    public User(String userId){
        this.userId = userId;
    }

}
