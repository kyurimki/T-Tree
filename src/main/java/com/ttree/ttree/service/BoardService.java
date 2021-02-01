package com.ttree.ttree.service;

import com.ttree.ttree.domain.entity.Board;
import com.ttree.ttree.domain.entity.Language;
import com.ttree.ttree.domain.repository.BoardRepository;
import com.ttree.ttree.dto.BoardDto;
import com.ttree.ttree.dto.LanguageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {
    private BoardRepository boardRepository;

    private static final int BLOCK_PAGE_NUM_COUNT = 5;
    private static final int PAGE_POST_COUNT = 4;

    List<Board> boardList = null;
    List<Board> boardListByYear = null;

    List<BoardDto> boardDtoList = new ArrayList<>();

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public Long savePost(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();
    }

    @Transactional
    public List<BoardDto> getBoardList(String index, String toSearch) {
        /*
        Page<Board> page = boardRepository.findAll(PageRequest.of(pageNum-1, PAGE_POST_COUNT,
                Sort.by(Sort.Direction.ASC, "createdDate")));
        List<Board> boardList = page.getContent();

         */
        boardList = boardRepository.findAll();
        boardListByYear = boardRepository.findByYear(toSearch);

        List<BoardDto> boardDtoList = new ArrayList<>();
        if (index == null) {
            for (Board board : boardList) {
                BoardDto boardDto = BoardDto.builder()
                        .id(board.getId())
                        .title(board.getTitle())
                        .year(board.getYear())
                        .semester(board.getSemester())
                        .content(board.getContent())
                        .createdDate(board.getCreatedDate())
                        .hit(board.getHit())
                        .build();
                boardDtoList.add(boardDto);
            }
        } else if (index.equals("year")) {
            for (Board board : boardListByYear) {
                BoardDto boardDto = BoardDto.builder()
                        .id(board.getId())
                        .title(board.getTitle())
                        .year(board.getYear())
                        .semester(board.getSemester())
                        .content(board.getContent())
                        .createdDate(board.getCreatedDate())
                        .hit(board.getHit())
                        .build();
                boardDtoList.add(boardDto);

            }
        }
        return boardDtoList;
    }

    @Transactional
    public List<BoardDto> getBoardListFromLang(String[] languageSet, List<BoardDto> boardDtoList, LanguageService languageService) {

       /*
        Page<Board> page = boardRepository.findAll(PageRequest.of(pageNum-1, PAGE_POST_COUNT,
                Sort.by(Sort.Direction.ASC, "createdDate")));
        List<Board> boardList = page.getContent();

        */

        boardList = boardRepository.findAll();
        if (boardDtoList != null) {
            for (int i = 0; i < boardDtoList.size(); i++) {
                Long id = boardDtoList.get(i).getId();
                LanguageDto languageDto = languageService.getLanguage(id);
                boolean status = true;
                for (String lang : languageSet) {
                    if (lang.equals("android")) {
                        status = languageDto.isLang_android();
                    } else if (lang.equals("cpp")) {
                        status = languageDto.isLang_cpp();
                    } else if (lang.equals("django")) {
                        status = languageDto.isLang_django();
                    } else if (lang.equals("html")) {
                        status = languageDto.isLang_html();
                    } else if (lang.equals("java")) {
                        status = languageDto.isLang_java();
                    } else if (lang.equals("nodejs")) {
                        status = languageDto.isLang_nodejs();
                    } else if (lang.equals("python")) {
                        status = languageDto.isLang_python();
                    } else if (lang.equals("rn")) {
                        status = languageDto.isLang_react();
                    } else if (lang.equals("spring")) {
                        status = languageDto.isLang_spring();
                    } else if (lang.equals("vuejs")) {
                        status = languageDto.isLang_vuejs();
                    } else if (lang.equals("etc")) {
                        String etcStatus = languageDto.getLang_etc();
                        if (etcStatus == null) {
                            status = false;
                        } else {
                            status = true;
                        }
                    }
                }
                if (!status) {
                    boardDtoList.remove(i);
                    i--;
                }
            }
        }
        return boardDtoList;
    }

    /*

    public Integer[] getPageList(Integer curPageNum){
        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];
        
        //총 게시물 개수
        Double postsTotalCount = Double.valueOf(this.getBoardCount());
        
        //총 게시글 기준으로 계산한 마지막 페이지 계산
        Integer totalLastPageNum = (int)(Math.ceil((postsTotalCount/PAGE_POST_COUNT)));

        //현재 페이지를 기준으로 블럭의 마지막 페이지 번호 계산
        Integer blockLastPageNum = (totalLastPageNum > curPageNum + BLOCK_PAGE_NUM_COUNT)
                ? curPageNum + BLOCK_PAGE_NUM_COUNT
                : totalLastPageNum;
        
        //페이지 시작 번호 조정
        curPageNum = (curPageNum <=3) ? 1 : curPageNum-2;
        
        //페이지 번호 할당
        for(int val = curPageNum, i = 0; val <= blockLastPageNum; val++, i++){
            pageList[i] = val;
        }

        return pageList;
    }

    @Transactional
    public Long getBoardCount(){
        return boardRepository.count();
    }

     */


    @Transactional
    public BoardDto getPost(Long id) {
        Board board = boardRepository.findById(id).get();

        BoardDto boardDto = BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .year(board.getYear())
                .semester(board.getSemester())
                .purpose(board.getPurpose())
                .content(board.getContent())
                .effect(board.getEffect())
                .createdDate(board.getCreatedDate())
                .hit(board.getHit())
                .build();
        return boardDto;
    }

    @Transactional
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }
}