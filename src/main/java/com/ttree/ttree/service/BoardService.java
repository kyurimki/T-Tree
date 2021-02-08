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
import java.util.Arrays;
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
    public List<BoardDto> getBoardDtoList(List<String> yearToSearch, List<String> langToSearch) {
                /*
        Page<Board> page = boardRepository.findAll(PageRequest.of(pageNum-1, PAGE_POST_COUNT,
                Sort.by(Sort.Direction.ASC, "createdDate")));
        List<Board> boardList = page.getContent();

         */
        List<Board> boardList = new ArrayList<>();
        List<BoardDto> boardDtoList = new ArrayList<>();
        List<String> langList = Arrays.asList("Android", "C/C++", "Django", "HTML5", "Java", "NodeJS", "Python", "React-Native", "Spring", "VueJS");

        List<Board> tmpList = boardRepository.findAll();

        if(yearToSearch != null) {
            for(String year : yearToSearch) {
                for(Board board : tmpList) {
                    if(year.equals(board.getYear())) {
                        boardList.add(board);
                    }
                }
            }
        } else {
            boardList = tmpList;
        }

        tmpList = boardList;

        if(langToSearch != null) {
            if(langToSearch.get(langToSearch.size()-1).equals("etc")) {
                int i = 0;
                while(i < tmpList.size()) {
                    if(langList.contains(tmpList.get(i).getLanguages().get(tmpList.get(i).getLanguages().size()-1))) {
                       boardList.remove(tmpList.get(i));
                    } else {
                        i++;
                    }
                }
                langToSearch = langToSearch.subList(0, langToSearch.size()-1);
                tmpList = boardList;
            }

            int j = 0;
            while(j  < tmpList.size()) {
                int k = 0;
                while(k < langToSearch.size()) {
                    if(tmpList.get(j).getLanguages().contains(langToSearch.get(k))) {
                        k++;
                    } else {
                        boardList.remove(tmpList.get(j));
                        j--;
                        break;
                    }
                }
                j++;
            }
        }

        for (Board board : boardList) {
            BoardDto boardDto = BoardDto.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .year(board.getYear())
                    .semester(board.getSemester())
                    .content(board.getContent())
                    .createdDate(board.getCreatedDate())
                    .hit(board.getHit())
                    .languages(board.getLanguages())
                    .build();
            boardDtoList.add(boardDto);
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
                .languages(board.getLanguages())
                .build();
        return boardDto;
    }

    @Transactional
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }
}