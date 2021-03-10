package ttree.it.ttreeGradle.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ttree.it.ttreeGradle.domain.entity.ProposalFile;

public interface ProposalFileRepository extends JpaRepository<ProposalFile, Long> {
}
