package ttree.it.ttreeGradle.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ttree.it.ttreeGradle.domain.entity.PaperFile;

public interface PaperFileRepository extends JpaRepository<PaperFile, Long> {
}
