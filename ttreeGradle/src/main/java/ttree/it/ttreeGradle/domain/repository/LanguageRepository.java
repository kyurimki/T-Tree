package ttree.it.ttreeGradle.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ttree.it.ttreeGradle.domain.entity.Language;

public interface LanguageRepository extends JpaRepository<Language, Long> {
}