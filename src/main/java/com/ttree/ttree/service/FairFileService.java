package com.ttree.ttree.service;

import com.ttree.ttree.domain.entity.FairFile;
import com.ttree.ttree.domain.repository.FairFileRepository;
import com.ttree.ttree.dto.FairFileDto;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
public class FairFileService {
    private FairFileRepository fairFileRepository;

    public FairFileService(FairFileRepository fairFileRepository){
        this.fairFileRepository = fairFileRepository;
    }

    @Transactional
    public void saveFairFile(FairFileDto fairFileDto){
        fairFileRepository.save(fairFileDto.toEntity());
    }

    @Transactional
    public FairFileDto getFairFile(Long id){
        try {
            FairFile fairFile = fairFileRepository.findById(id).get();

            FairFileDto fairFileDto = FairFileDto.builder()
                    .fair_id(id)
                    .fair_origFilename(fairFile.getFair_origFilename())
                    .fair_filename(fairFile.getFair_filename())
                    .fair_filePath(fairFile.getFair_filePath())
                    .build();
            return fairFileDto;
        } catch(NoSuchElementException e) {
            return null;
        }

    }

    @Transactional
    public void deleteFairFile(Long id) { fairFileRepository.deleteById(id);}
}
