package com.ttree.ttree.dto;

import com.ttree.ttree.domain.entity.SourceFile;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SourceFileDto {
    private Long source_id;
    private String source_origFilename;
    private String source_filename;
    private String source_filePath;

    public SourceFile toEntity() {
        SourceFile build = SourceFile.builder()
                .source_id(source_id)
                .source_origFilename(source_origFilename)
                .source_filename(source_filename)
                .source_filePath(source_filePath)
                .build();
        return build;
    }

    @Builder
    public SourceFileDto(Long source_id, String source_origFilename, String source_filename, String source_filePath) {
        this.source_id = source_id;
        this.source_origFilename = source_origFilename;
        this.source_filename = source_filename;
        this.source_filePath = source_filePath;
    }
}