package com.example.miniproject.service;

import com.example.miniproject.config.security.UserDetailsImp;
import com.example.miniproject.dto.BoardRequestDto;
import com.example.miniproject.dto.BoardResponseDto;
import com.example.miniproject.dto.FilterRequestDto;
import com.example.miniproject.dto.http.DefaultDataRes;
import com.example.miniproject.dto.http.DefaultRes;
import com.example.miniproject.dto.http.ResponseMessage;
import com.example.miniproject.dto.http.StatusCode;
import com.example.miniproject.entity.Board;
import com.example.miniproject.exception.CustomException;
import com.example.miniproject.repository.BoardRepository;
import com.example.miniproject.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    private final S3Uploader s3Uploader;

    @Transactional
    public ResponseEntity<?> createBoard(BoardRequestDto boardRequestDto, UserDetailsImp userDetailsImp) {
        // 입력값 중 하나라도 null 이면 exception 처리
        if (boardRequestDto.getTitle() == null || boardRequestDto.getImage() == null || boardRequestDto.getStar() == null || boardRequestDto.getLocation() == null || boardRequestDto.getPlacename() == null || boardRequestDto.getContent() == null || boardRequestDto.getSeason() == null) {
            throw new CustomException(ResponseMessage.WRONG_FORMAT, StatusCode.BAD_REQUEST);
        }

        try { // upload method 에서 발생하는 IOException 을 Customize 하기 위해 try-catch 사용
            String imgPath = s3Uploader.upload(boardRequestDto.getImage());
            Board board = new Board(boardRequestDto, imgPath);
            board.setUser(userDetailsImp.getUser());
            boardRepository.save(board);
            BoardResponseDto boardResponseDto = new BoardResponseDto(board);
            //수정
            return ResponseEntity.ok(new DefaultDataRes<BoardResponseDto>(ResponseMessage.BOARD_CREATE, boardResponseDto));
        } catch (IOException e) {
            throw new CustomException(ResponseMessage.S3_ERROR, StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getBoarsWithFilter(FilterRequestDto filterRequestDto) {
        List<Board> boardList = boardRepository.search(filterRequestDto);
        List<BoardResponseDto> boardResponseDtoList = boardList.stream().map(BoardResponseDto::new).toList();
        //수정
        return ResponseEntity.ok(new DefaultDataRes<List<BoardResponseDto>>(ResponseMessage.BOARD_GET, boardResponseDtoList));
    }
  
    @Transactional
    public ResponseEntity<?> deleteBoard(Long boardId, UserDetailsImp userDetails) {
        Board board = findBoardOrElseThrow(boardId, ResponseMessage.BOARD_DELETE_FAIL);

        if (!board.getUser().getUserId().equals(userDetails.getUser().getUserId())) {
            throw new CustomException(ResponseMessage.BOARD_DELETE_FAIL, StatusCode.BAD_REQUEST);
        }

        String imgPath = board.getImage();
        if (s3Uploader.delete(imgPath)) { // S3 에서 이미지 파일 삭제가 성공하면 DB에 있는 게시글도 삭제
            boardRepository.delete(board);
            return ResponseEntity.ok(new DefaultRes<>(ResponseMessage.BOARD_DELETE));
        }

        throw new CustomException(ResponseMessage.BOARD_DELETE_FAIL, StatusCode.INTERNAL_SERVER_ERROR);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getBoard(Long boardId) {
        Board board = findBoardOrElseThrow(boardId, ResponseMessage.BOARD_GET_FAIL_ID);
        BoardResponseDto boardResponseDto = new BoardResponseDto(board);
        return ResponseEntity.ok(new DefaultDataRes<>(ResponseMessage.BOARD_GET_ID, boardResponseDto));
    }

    @Transactional
    public ResponseEntity<?> updateBoard(Long boardId, BoardRequestDto boardRequestDto, UserDetailsImp userDetails) {
        Board board = findBoardOrElseThrow(boardId, ResponseMessage.BOARD_UPDATE_FAIL);

        if (!board.getUser().getUserId().equals(userDetails.getUser().getUserId())) {
            throw new CustomException(ResponseMessage.BOARD_UPDATE_FAIL, StatusCode.BAD_REQUEST);
        }

        // 기존 이미지 삭제
        String imgUrl = board.getImage();
        s3Uploader.delete(imgUrl);

        // 새로운 이미지로 저장
        try { // upload method 에서 발생하는 IOException 을 Customize 하기 위해 try-catch 사용
            String imgPath = s3Uploader.upload(boardRequestDto.getImage());
            board.update(boardRequestDto, imgPath);
            BoardResponseDto boardResponseDto = new BoardResponseDto(board);
            return ResponseEntity.ok(new DefaultRes<BoardResponseDto>(ResponseMessage.BOARD_UPDATE));
        } catch (IOException e) {
            throw new CustomException(ResponseMessage.S3_ERROR, StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    public Board findBoardOrElseThrow(Long boardId, String msg) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(msg, StatusCode.BAD_REQUEST)
        );
    }
}
