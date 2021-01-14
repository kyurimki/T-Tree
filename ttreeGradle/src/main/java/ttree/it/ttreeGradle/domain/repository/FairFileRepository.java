package ttree.it.ttreeGradle.domain.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ttree.it.ttreeGradle.domain.entity.FairFile;

public interface FairFileRepository extends JpaRepository<FairFile, Long> {
}
