package com.ttree.ttree.domain.repository;

import com.ttree.ttree.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>{

}