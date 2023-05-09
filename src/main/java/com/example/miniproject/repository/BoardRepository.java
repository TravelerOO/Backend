package com.example.miniproject.repository;

import com.example.miniproject.entity.Board;
import com.example.miniproject.repository.search.BoardSearch;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board, Long> , BoardSearch {
}