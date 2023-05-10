package com.example.miniproject.dto;

import lombok.Getter;

@Getter
public class LoginResponseDto {
    private String nickname;

    public LoginResponseDto(String nickname) {
        this.nickname = nickname;
    }
}
