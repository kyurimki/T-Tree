package com.ttree.ttree.service;

import com.ttree.ttree.domain.entity.SourceFile;
import com.ttree.ttree.domain.repository.SourceFileRepository;
import com.ttree.ttree.dto.SourceFileDto;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

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
        SourceFile sourceFile = sourceFileRepository.findById(id).get();

        SourceFileDto fileDto = SourceFileDto.builder()
                .source_id(id)
                .source_origFilename(sourceFile.getSource_origFilename())
                .source_filename(sourceFile.getSource_filename())
                .source_filePath(sourceFile.getSource_filePath())
                .build();
        return fileDto;
    }

    @Transactional
    public void deleteSourceFile(Long id) {
        sourceFileRepository.deleteById(id);
    }
}
