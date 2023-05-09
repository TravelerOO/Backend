package com.example.miniproject.controller;


import com.example.miniproject.config.security.UserDetailsImp;
import com.example.miniproject.dto.BoardRequestDto;
import com.example.miniproject.dto.BoardResponseDto;
import com.example.miniproject.dto.FilterRequestDto;
import com.example.miniproject.dto.MsgAndHttpStatusDto;
import com.example.miniproject.service.BoardService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
    public ResponseEntity<List<BoardResponseDto>> getBoardsWithFilter(@ModelAttribute FilterRequestDto filterRequestDto) {
        return boardService.getBoarsWithFilter(filterRequestDto);
    }

    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<MsgAndHttpStatusDto> deleteBoard(@PathVariable Long boardId, @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImp userDetails) {
        return boardService.deleteBoard(boardId, userDetails);
    }
}
