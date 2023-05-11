package com.example.miniproject.dto;

import lombok.Data;

@Data
public class BoardUpdateRequestDto {
    private String title;
    private Integer star;
    private String location;
    private String placename;
    private String content;
    private String season;
}
