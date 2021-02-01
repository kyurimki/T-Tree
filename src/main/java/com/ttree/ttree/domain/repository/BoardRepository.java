package com.ttree.ttree.domain.repository;

import com.ttree.ttree.domain.entity.Board;
import com.ttree.ttree.dto.BoardDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>{
    List<Board> findByYear(String year);
    //List<Board> findByLanguage(Boolean checked);

}