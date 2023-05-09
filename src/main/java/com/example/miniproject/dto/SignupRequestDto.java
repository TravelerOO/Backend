package com.example.miniproject.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignupRequestDto {

    @NotNull(message = "id를 입력해주세요")
    @Size(min =6, max = 18, message = "id는 소문자, 숫자로 6자~18자 이하만 가능합니다.")
    @Pattern(regexp = "^[a-z0-9]{6,18}$")
    private String userId;

    @NotNull(message = "password를 입력해주세요")
    @Size(min = 8, max = 20, message = "password는 영문, 숫자, 특수문자로 8자~20자 이하만 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9@$!%*#?&]{8,20}$")

    private String password;
    private String nickname;

}
