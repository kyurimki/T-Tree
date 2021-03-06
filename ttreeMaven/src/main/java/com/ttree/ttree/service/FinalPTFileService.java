package com.ttree.ttree.service;

import com.ttree.ttree.domain.entity.FinalPTFile;
import com.ttree.ttree.domain.repository.FinalPTFileRepository;
import com.ttree.ttree.dto.FinalPTFileDto;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
public class FinalPTFileService {
    private FinalPTFileRepository finalPTFileRepository;

    public FinalPTFileService(FinalPTFileRepository finalPTFileRepository){
        this.finalPTFileRepository = finalPTFileRepository;
    }

    @Transactional
    public void saveFinalPTFile(FinalPTFileDto finalPTFileDto){
        finalPTFileRepository.save(finalPTFileDto.toEntity());
    }

    @Transactional
    public FinalPTFileDto getFinalPTFile(Long id){
        try {
            FinalPTFile finalPTFile = finalPTFileRepository.findById(id).get();

            FinalPTFileDto finalPTFileDto = FinalPTFileDto.builder()
                    .finalPT_id(id)
                    .finalPT_origFilename(finalPTFile.getFinalPT_origFilename())
                    .finalPT_filename(finalPTFile.getFinalPT_filename())
                    .finalPT_filePath(finalPTFile.getFinalPT_filePath())
                    .build();
            return finalPTFileDto;
        } catch(NoSuchElementException e) {
            return null;
        }

    }

    @Transactional
    public void deleteFinalPTFile(Long id){
        if(finalPTFileRepository.findById(id).isPresent()) {
            finalPTFileRepository.deleteById(id);
        }else{
            System.out.println("file is already empty");
        }
    }

}
