package ttree.it.ttreeGradle.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ttree.it.ttreeGradle.domain.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{

}