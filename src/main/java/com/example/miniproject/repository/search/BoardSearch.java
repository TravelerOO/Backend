package com.example.miniproject.repository.search;

import com.example.miniproject.dto.FilterRequestDto;
import com.example.miniproject.entity.Board;

import java.util.List;

public interface BoardSearch {
    List<Board> search(FilterRequestDto filterRequestDto);
}