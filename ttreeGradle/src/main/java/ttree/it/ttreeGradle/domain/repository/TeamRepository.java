package ttree.it.ttreeGradle.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ttree.it.ttreeGradle.domain.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
