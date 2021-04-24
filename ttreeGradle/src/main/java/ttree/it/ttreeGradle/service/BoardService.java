package ttree.it.ttreeGradle.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ttree.it.ttreeGradle.domain.entity.Board;
import ttree.it.ttreeGradle.domain.repository.BoardRepository;
import ttree.it.ttreeGradle.dto.BoardDto;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class BoardService {
    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Page<Board> getBoardList(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
        pageable = PageRequest.of(page, 10);

        return boardRepository.findAll(pageable);
    }

    @Transactional
    public Long savePost(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();
    }

    @Transactional
    public List<BoardDto> getBoardDtoList(List<String> yearToSearch, List<String> langToSearch, List<String> typeToSearch) {

        List<Board> boardList = new ArrayList<>();
        List<BoardDto> boardDtoList = new ArrayList<>();

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
            if(langToSearch.get(langToSearch.size()-1).equals("langEtc")) {
                int i = 0;
                while(i < tmpList.size()) {
                    if(tmpList.get(i).getLangEtc() == null) {
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

        tmpList = boardList;

        if(typeToSearch != null) {
            if(typeToSearch.get(typeToSearch.size()-1).equals("typeEtc")) {
                int i = 0;
                while(i < tmpList.size()) {
                    if(tmpList.get(i).getTypeEtc() == null) {
                        boardList.remove(tmpList.get(i));
                    } else {
                        i++;
                    }
                }
                typeToSearch = typeToSearch.subList(0, typeToSearch.size()-1);
                tmpList = boardList;
                System.out.println(tmpList.size());
            }

            int j = 0;
            while(j  < tmpList.size()) {
                int k = 0;
                while(k < typeToSearch.size()) {
                    if(tmpList.get(j).getTypes().contains(typeToSearch.get(k))) {
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

        Collections.sort(boardList);

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
                    .types(board.getTypes())
                    .langEtc(board.getLangEtc())
                    .typeEtc(board.getTypeEtc())
                    .link(board.getLink())
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
                .purpose(board.getPurpose())
                .content(board.getContent())
                .effect(board.getEffect())
                .createdDate(board.getCreatedDate())
                .hit(board.getHit())
                .languages(board.getLanguages())
                .types(board.getTypes())
                .langEtc(board.getLangEtc())
                .typeEtc(board.getTypeEtc())
                .link(board.getLink())
                .build();
        return boardDto;
    }

    @Transactional
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }
}