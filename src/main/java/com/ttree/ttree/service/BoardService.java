package com.ttree.ttree.service;

import com.ttree.ttree.domain.entity.Board;
import com.ttree.ttree.domain.repository.BoardRepository;
import com.ttree.ttree.dto.BoardDto;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {
    private BoardRepository boardRepository;
    List<Board> boardList = null;
    List<BoardDto> boardDtoList = new ArrayList<>();

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public void savePost(BoardDto boardDto) {
        boardRepository.save(boardDto.toEntity());
    }

    @Transactional
    public List<BoardDto> getBoardList(String index, String toSearch) {
        System.out.println("!");
        if(index.equals("year")) {
            switch(toSearch) {
                case "2016":
                    boardList = boardRepository.findBoardsByYearIs("2016");
                case "2017":
                    boardList = boardRepository.findBoardsByYearIs("2017");
                case "2018":
                    boardList = boardRepository.findBoardsByYearIs("2018");
                case "2019":
                    boardList = boardRepository.findBoardsByYearIs("2019");
                case "2020":
                    boardList = boardRepository.findBoardsByYearIs("2020");
            }
        }

        for(Board board : boardList) {
            BoardDto boardDto = BoardDto.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .year(board.getYear())
                    .semester(board.getSemester())
                    .content(board.getContent())
                    .createdDate(board.getCreatedDate())
                    .build();
            boardDtoList.add(boardDto);
        }
        return boardDtoList;
    }

    @Transactional
    public List<BoardDto> getBoardList() {
        boardList = boardRepository.findAll();

        List<BoardDto> boardDtoList = new ArrayList<>();

        for(Board board : boardList) {
            BoardDto boardDto = BoardDto.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .year(board.getYear())
                    .semester(board.getSemester())
                    .content(board.getContent())
                    .createdDate(board.getCreatedDate())
                    .build();
            boardDtoList.add(boardDto);
        }
        return boardDtoList;
    }

    @Transactional
    public BoardDto getPost(Long id) {
        Board board = boardRepository.findById(id).get();

        BoardDto boardDto = BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .year(board.getYear())
                .semester(board.getSemester())
                .content(board.getContent())
                .createdDate(board.getCreatedDate())
                .build();
        return boardDto;
    }

    @Transactional
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }
}