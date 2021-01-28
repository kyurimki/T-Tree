package com.ttree.ttree.service;

import com.ttree.ttree.domain.entity.SourceFile;
import com.ttree.ttree.domain.repository.SourceFileRepository;
import com.ttree.ttree.dto.SourceFileDto;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
public class SourceFileService {
    private SourceFileRepository sourceFileRepository;

    public SourceFileService(SourceFileRepository sourceFileRepository) {
        this.sourceFileRepository = sourceFileRepository;
    }

    @Transactional
    public void saveSourceFile(SourceFileDto sourceFileDto) {
        sourceFileRepository.save(sourceFileDto.toEntity());
    }

    @Transactional
    public SourceFileDto getSourceFile(Long id) {
        try {
            SourceFile sourceFile = sourceFileRepository.findById(id).get();

            SourceFileDto fileDto = SourceFileDto.builder()
                    .source_id(id)
                    .source_origFilename(sourceFile.getSource_origFilename())
                    .source_filename(sourceFile.getSource_filename())
                    .source_filePath(sourceFile.getSource_filePath())
                    .build();
            return fileDto;
        } catch(NoSuchElementException e) {
            return null;
        }
    }

    @Transactional
    public void deleteSourceFile(Long id) {
        if(sourceFileRepository.findById(id).isPresent()) {
            sourceFileRepository.deleteById(id);
        }else{
            System.out.println("file is already empty");
        }
    }
}
