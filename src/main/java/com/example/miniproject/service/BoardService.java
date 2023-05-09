package com.example.miniproject.service;

import com.example.miniproject.config.security.UserDetailsImp;
import com.example.miniproject.dto.BoardRequestDto;
import com.example.miniproject.dto.BoardResponseDto;
import com.example.miniproject.dto.FilterRequestDto;
import com.example.miniproject.dto.MsgAndHttpStatusDto;
import com.example.miniproject.dto.http.DefaultDataRes;
import com.example.miniproject.dto.http.DefaultRes;
import com.example.miniproject.dto.http.ResponseMessage;
import com.example.miniproject.dto.http.StatusCode;
import com.example.miniproject.entity.Board;
import com.example.miniproject.repository.BoardRepository;
import com.example.miniproject.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    private final S3Uploader s3Uploader;

    @Transactional
    public ResponseEntity<?> createBoard(BoardRequestDto boardRequestDto, UserDetailsImp userDetailsImp) throws IOException {
        // S3 에 이미지 저장(MultiPartFile 이 거치는 임시 저장 경로에서 tmp 파일이 삭제 안 되는 상황, S3및 DB 저장은 성공)
        if (boardRequestDto.getImage() != null) {
            String imgPath = s3Uploader.upload(boardRequestDto.getImage());
            Board board = new Board(boardRequestDto, imgPath);
            board.setUser(userDetailsImp.getUser());
            boardRepository.save(board);
            BoardResponseDto boardResponseDto = new BoardResponseDto(board);

            //수정
            return ResponseEntity.ok(DefaultDataRes.dataRes(StatusCode.OK, ResponseMessage.BOARD_CREATE,boardResponseDto));
        }

        throw new IllegalArgumentException("이미지 파일을 업로드해주세요");
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getBoarsWithFilter(FilterRequestDto filterRequestDto) {
        List<Board> boardList = null;

        if (filterRequestDto.getLocation() == null && filterRequestDto.getStar() == null & filterRequestDto.getKeyword() == null) { // keyword : x, location : x, star : x
            boardList = boardRepository.findAllBySeasonOrderByCreatedAtDesc(filterRequestDto.getSeason());
        } else { // 2. 그 외 필터 조건들 있을 경우 조건 모두 반영
            if (filterRequestDto.getKeyword() != null) {
                if (filterRequestDto.getLocation() == null && filterRequestDto.getStar() == null) { // keyword : o, location : x, star : x
                    boardList = boardRepository.findAllBySeasonAndContainingKeywordOrderByCreatedAtDesc(filterRequestDto.getSeason(), filterRequestDto.getKeyword());
                } else if (filterRequestDto.getLocation() != null && filterRequestDto.getStar() == null) { // keyword : o, location : o, star : x
                    boardList = boardRepository.findAllBySeasonAndLocationAndContainingKeywordOrderByCreatedAtDesc(filterRequestDto.getSeason(), filterRequestDto.getLocation(), filterRequestDto.getKeyword());
                } else if (filterRequestDto.getLocation() == null && filterRequestDto.getStar() != null) { // // keyword : o, location : x, star : o
                    if (filterRequestDto.getStar().equals("asc")) {
                        boardList = boardRepository.findAllBySeasonAndContainingKeywordOrderByStar(filterRequestDto.getSeason(), filterRequestDto.getKeyword());
                    } else {
                        boardList = boardRepository.findAllBySeasonAndContainingKeywordOrderByStarDesc(filterRequestDto.getSeason(), filterRequestDto.getKeyword());
                    }
                } else { // keyword : o, location : o, star : o
                    if (filterRequestDto.getStar().equals("asc")) {
                        boardList = boardRepository.findAllBySeasonAndLocationAndContainingKeywordOrderByStar(filterRequestDto.getSeason(), filterRequestDto.getLocation(), filterRequestDto.getKeyword());
                    } else {
                        boardList = boardRepository.findAllBySeasonAndLocationAndContainingKeywordOrderByStarDesc(filterRequestDto.getSeason(), filterRequestDto.getLocation(), filterRequestDto.getKeyword());
                    }
                }
            } else {
                if (filterRequestDto.getLocation() != null && filterRequestDto.getStar() == null) { // keyword : x, location : o, star : x
                    boardList = boardRepository.findAllBySeasonAndLocationOrderByCreatedAtDesc(filterRequestDto.getSeason(), filterRequestDto.getLocation());
                } else if (filterRequestDto.getLocation() == null && filterRequestDto.getStar() != null) { // keyword : x, location : x, star : o
                    if (filterRequestDto.getStar().equals("asc")) {
                        boardList = boardRepository.findAllBySeasonOrderByStar(filterRequestDto.getSeason());
                    } else {
                        boardList = boardRepository.findAllBySeasonOrderByStarDesc(filterRequestDto.getSeason());
                    }
                } else { // keyword : x, location : o, star : o
                    if (filterRequestDto.getStar().equals("asc")) {
                        boardList = boardRepository.findAllBySeasonAndLocationOrderByStar(filterRequestDto.getSeason(), filterRequestDto.getLocation());
                    } else {
                        boardList = boardRepository.findAllBySeasonAndLocationOrderByStarDesc(filterRequestDto.getSeason(), filterRequestDto.getLocation());
                    }
                }
            }
        }
        List<BoardResponseDto> boardResponseDtoList = boardList.stream().map(BoardResponseDto::new).toList();
        //수정
        return ResponseEntity.ok(DefaultDataRes.dataRes(StatusCode.OK, ResponseMessage.BOARD_CREATE,boardResponseDtoList));
    }

    @Transactional
    public ResponseEntity<?> deleteBoard(Long boardId, UserDetailsImp userDetails) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new NullPointerException("존재하지 않는 게시글입니다.")
        );

        if (!board.getUser().getUserId().equals(userDetails.getUser().getUserId())) {
            throw new IllegalArgumentException("본인이 작성한 글만 삭제할 수 있습니다.");
        }

        String imgPath = board.getImage();
        if (s3Uploader.delete(imgPath)) { // S3 에서 이미지 파일 삭제가 성공하면 DB에 있는 게시글도 삭제
            boardRepository.delete(board);
            return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.BOARD_DELETE));
        }

        throw new IllegalArgumentException("삭제에 실패했습니다.");
    }
}
