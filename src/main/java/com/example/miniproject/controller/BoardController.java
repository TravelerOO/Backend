package com.example.miniproject.controller;


import com.example.miniproject.config.security.UserDetailsImp;
import com.example.miniproject.dto.BoardRequestDto;
import com.example.miniproject.dto.FilterRequestDto;
import com.example.miniproject.service.BoardService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping(value = "/board", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBoard(@ModelAttribute BoardRequestDto boardRequestDto, @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImp userDetails) throws IOException {
        return boardService.createBoard(boardRequestDto, userDetails);
    }

    @GetMapping("/boards")
    public ResponseEntity<?> getBoardsWithFilter(@ModelAttribute FilterRequestDto filterRequestDto) {
        return boardService.getBoarsWithFilter(filterRequestDto);
    }

    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId, @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImp userDetails) {
        return boardService.deleteBoard(boardId, userDetails);
    }

    @GetMapping("/boards/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId) {
        return boardService.getBoard(boardId);
    }

    @PutMapping(value = "/boards/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateBoard(@PathVariable Long boardId, @ModelAttribute BoardRequestDto boardRequestDto, @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImp userDetails) {
        return boardService.updateBoard(boardId, boardRequestDto, userDetails);
    }
}
