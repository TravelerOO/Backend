package com.example.miniproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private String nickname;

    public LoginResponseDto(String nickname) {
        this.nickname = nickname;
    }
}
