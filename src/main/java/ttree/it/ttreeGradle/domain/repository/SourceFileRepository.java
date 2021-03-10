package ttree.it.ttreeGradle.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ttree.it.ttreeGradle.domain.entity.SourceFile;

public interface SourceFileRepository extends JpaRepository<SourceFile, Long> {
}