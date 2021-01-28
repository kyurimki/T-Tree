package com.ttree.ttree.service;

import com.ttree.ttree.domain.entity.ProposalFile;
import com.ttree.ttree.domain.repository.ProposalFileRepository;
import com.ttree.ttree.dto.ProposalFileDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
public class ProposalFileService {
    private ProposalFileRepository proposalFileRepository;

    public ProposalFileService(ProposalFileRepository proposalFileRepository){
        this.proposalFileRepository = proposalFileRepository;
    }

    @Transactional
    public void saveProposalFile(ProposalFileDto proposalFileDto){
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
    public void deleteProposalFile(Long id) {
        if (proposalFileRepository.findById(id).isPresent()) {
            proposalFileRepository.deleteById(id);
        } else{
            System.out.println("file is already empty");
        }
    }
}
