package com.example.miniproject.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignupRequestDto {

    @Pattern(regexp = "^[a-z0-9]{6,18}$", message = "id는 소문자, 숫자로 6자~18자 이하만 가능합니다.")
    private String userId;

    @Pattern(regexp = "^[a-zA-Z0-9@$!%*#?&]{8,20}$", message = "password는 영문, 숫자, 특수문자로 8자~20자 이하만 가능합니다.")
    private String password;

    private String nickname;

}
