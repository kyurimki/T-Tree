package com.ttree.ttree.service;

import com.ttree.ttree.domain.entity.PaperFile;
import com.ttree.ttree.domain.repository.PaperFileRepository;
import com.ttree.ttree.dto.PaperFileDto;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
public class PaperFileService {
    private PaperFileRepository paperFileRepository;

    public PaperFileService(PaperFileRepository paperFileRepository) {
        this.paperFileRepository = paperFileRepository;
    }

    @Transactional
    public void savePaperFile(PaperFileDto paperFileDto) {
        paperFileRepository.save(paperFileDto.toEntity());
    }

    @Transactional
    public PaperFileDto getPaperFile(Long id) {
        PaperFile paperFile = paperFileRepository.findById(id).get();

        PaperFileDto paperFileDto = PaperFileDto.builder()
                .paper_id(id)
                .paper_origFilename(paperFile.getPaper_origFilename())
                .paper_filename(paperFile.getPaper_filename())
                .paper_filePath(paperFile.getPaper_filePath())
                .build();
        return paperFileDto;
    }

    @Transactional
    public void deletePaperFile(Long id) {
        paperFileRepository.deleteById(id);
    }
}