package com.example.miniproject.repository.search;

import com.example.miniproject.dto.FilterRequestDto;
import com.example.miniproject.entity.Board;
import com.example.miniproject.entity.QBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class);
    }
    @Override
    public List<Board> search(FilterRequestDto filterRequestDto) {
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board); // select from board

        query.where(board.season.eq(filterRequestDto.getSeason()));

        if (filterRequestDto.getKeyword() != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            booleanBuilder.or(board.title.contains(filterRequestDto.getKeyword()));
            booleanBuilder.or(board.placename.contains(filterRequestDto.getKeyword()));
            booleanBuilder.or(board.content.contains(filterRequestDto.getKeyword()));
            query.where(booleanBuilder);
        }

        if (filterRequestDto.getLocation() != null) {
            query.where(board.location.eq(filterRequestDto.getLocation()));
        }

        if (filterRequestDto.getStar() != null) {
            if (filterRequestDto.getStar().equals("asc")) {
                query.orderBy(board.star.asc());
            } else {
                query.orderBy(board.star.desc());
            }
        } else {
            query.orderBy(board.modifiedAt.desc());
        }

        return query.fetch();
    }
}