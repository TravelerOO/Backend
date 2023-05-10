package com.example.miniproject.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class BoardRequestDto {
    private String title;

    private MultipartFile image;

    private Integer star;

    private String location;

    private String placename;

    private String content;

    private String season;
}
