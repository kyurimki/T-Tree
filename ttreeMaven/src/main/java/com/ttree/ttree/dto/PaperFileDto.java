package com.ttree.ttree.dto;

import com.ttree.ttree.domain.entity.PaperFile;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PaperFileDto {
    private Long paper_id;
    private String paper_origFilename;
    private String paper_filename;
    private String paper_filePath;

    public PaperFile toEntity() {
        PaperFile build = PaperFile.builder()
                .paper_id(paper_id)
                .paper_origFilename(paper_origFilename)
                .paper_filename(paper_filename)
                .paper_filePath(paper_filePath)
                .build();
        return build;
    }

    @Builder
    public PaperFileDto(Long paper_id, String paper_origFilename, String paper_filename, String paper_filePath) {
        this.paper_id = paper_id;
        this.paper_origFilename = paper_origFilename;
        this.paper_filename = paper_filename;
        this.paper_filePath = paper_filePath;
    }
}