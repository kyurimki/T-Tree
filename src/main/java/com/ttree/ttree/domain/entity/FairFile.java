package com.ttree.ttree.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FairFile {

    @Id
    private Long fair_id;

    @Column(nullable = false)
    private String fair_origFilename;

    @Column(nullable = false)
    private String fair_filename;

    @Column(nullable = false)
    private String fair_filePath;

    @Builder
    public FairFile(Long fair_id, String fair_origFilename, String fair_filename, String fair_filePath){
        this.fair_id = fair_id;
        this.fair_origFilename = fair_origFilename;
        this.fair_filename = fair_filename;
        this.fair_filePath = fair_filePath;
    }
}
