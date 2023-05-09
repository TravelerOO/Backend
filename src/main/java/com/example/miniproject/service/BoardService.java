package com.example.miniproject.service;

import com.example.miniproject.config.security.UserDetailsImp;
import com.example.miniproject.dto.BoardRequestDto;
import com.example.miniproject.dto.BoardResponseDto;
import com.example.miniproject.dto.FilterRequestDto;
import com.example.miniproject.dto.MsgAndHttpStatusDto;
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

            return ResponseEntity.ok(boardResponseDto);
        }

        throw new IllegalArgumentException("이미지 파일을 업로드해주세요");
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<BoardResponseDto>> getBoarsWithFilter(FilterRequestDto filterRequestDto) {
        List<Board> boardList = boardRepository.search(filterRequestDto);
        List<BoardResponseDto> boardResponseDtoList = boardList.stream().map(BoardResponseDto::new).toList();
        return ResponseEntity.ok(boardResponseDtoList);
    }
    @Transactional
    public ResponseEntity<MsgAndHttpStatusDto> deleteBoard(Long boardId, UserDetailsImp userDetails) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new NullPointerException("존재하지 않는 게시글입니다.")
        );

        if (!board.getUser().getUserId().equals(userDetails.getUser().getUserId())) {
            throw new IllegalArgumentException("본인이 작성한 글만 삭제할 수 있습니다.");
        }

        String imgPath = board.getImage();
        if (s3Uploader.delete(imgPath)) { // S3 에서 이미지 파일 삭제가 성공하면 DB에 있는 게시글도 삭제
            boardRepository.delete(board);
            return ResponseEntity.ok(new MsgAndHttpStatusDto("삭제 완료!", HttpStatus.OK.value()));
        }

        throw new IllegalArgumentException("삭제에 실패했습니다.");
    }
}
