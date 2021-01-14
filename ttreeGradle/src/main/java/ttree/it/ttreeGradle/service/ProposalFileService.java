package ttree.it.ttreeGradle.service;

import org.springframework.stereotype.Service;
import ttree.it.ttreeGradle.domain.entity.ProposalFile;
import ttree.it.ttreeGradle.domain.repository.ProposalFileRepository;
import ttree.it.ttreeGradle.dto.ProposalFileDto;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
public class ProposalFileService {
    private ProposalFileRepository proposalFileRepository;

    public ProposalFileService(ProposalFileRepository proposalFileRepository){
        this.proposalFileRepository = proposalFileRepository;
    }

    @Transactional
    public void saveProposalFile(
            ProposalFileDto proposalFileDto){
        proposalFileRepository.save(proposalFileDto.toEntity());
    }

    @Transactional
    public ProposalFileDto getProposalFile(Long id){
        try {
            ProposalFile proposalFile = proposalFileRepository.findById(id).get();

            ProposalFileDto proposalFileDto = ProposalFileDto.builder()
                    .proposal_id(id)
                    .proposal_origFilename(proposalFile.getProposal_origFilename())
                    .proposal_filename(proposalFile.getProposal_filename())
                    .proposal_filePath(proposalFile.getProposal_filePath())
                    .build();
            return proposalFileDto;
        } catch(NoSuchElementException e) {
            return null;
        }
    }

    @Transactional
    public void deleteProposalFile(Long id){
        proposalFileRepository.deleteById(id);
    }
}
