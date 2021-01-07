package com.ttree.ttree.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SourceFile {

    @Id
    @GeneratedValue
    private Long source_id;

    @Column(nullable = false)
    private String source_origFilename;

    @Column(nullable = false)
    private String source_filename;

    @Column(nullable = false)
    private String source_filePath;

    @Builder
    public SourceFile(Long source_id, String source_origFilename, String source_filename, String source_filePath) {
        this.source_id = source_id;
        this.source_origFilename = source_origFilename;
        this.source_filename = source_filename;
        this.source_filePath = source_filePath;
    }
}