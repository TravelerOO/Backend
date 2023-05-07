package com.example.miniproject.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserIdRequestDto {
    @NotNull(message = "id를 입력해주세요")
    @Size(min =4, max = 10, message = "id는 소문자, 숫자로 4자~10자 이하만 가능합니다.")
    @Pattern(regexp = "^[a-z0-9]{6,18}$")
    private String userId;
}
