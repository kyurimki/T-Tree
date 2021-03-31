package ttree.it.ttreeGradle.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ttree.it.ttreeGradle.domain.entity.ApplicationForm;

public interface ApplicationFormRepository extends JpaRepository<ApplicationForm, Long> {
}
