package com.ttree.ttree.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="finalpt_file")
public class FinalPTFile {

    @Id
    private Long finalPT_id;

    @Column(nullable = false)
    private String finalPT_origFilename;

    @Column(nullable = false)
    private String finalPT_filename;

    @Column(nullable = false)
    private String finalPT_filePath;

    @Builder
    public FinalPTFile(Long finalPT_id, String finalPT_origFilename, String finalPT_filename, String finalPT_filePath){
        this.finalPT_id = finalPT_id;
        this.finalPT_origFilename = finalPT_origFilename;
        this.finalPT_filename = finalPT_filename;
        this.finalPT_filePath = finalPT_filePath;
    }
}
