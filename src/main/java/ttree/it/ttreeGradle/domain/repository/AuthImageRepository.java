package ttree.it.ttreeGradle.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ttree.it.ttreeGradle.domain.entity.AuthImage;

public interface AuthImageRepository extends JpaRepository<AuthImage, Long> {
}
