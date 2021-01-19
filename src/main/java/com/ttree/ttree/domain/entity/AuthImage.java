package com.ttree.ttree.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthImage {

    @Id
    private Long image_id;

    @Column(nullable = false)
    private String origFilename;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String filePath;

    @Builder
    public AuthImage (Long image_id, String origFilename, String filename, String filePath){
        this.image_id = image_id;
        this.origFilename = origFilename;
        this.filename = filename;
        this.filePath = filePath;
    }
}
