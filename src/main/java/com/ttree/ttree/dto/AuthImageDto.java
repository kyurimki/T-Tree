package com.ttree.ttree.dto;

import com.ttree.ttree.domain.entity.AuthImage;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AuthImageDto {
    private Long id;
    private String origFilename;
    private String filename;
    private String filePath;

    public AuthImage toEntity(){
        AuthImage build = AuthImage.builder()
                .id(id)
                .origFilename(origFilename)
                .filename(filename)
                .filePath(filePath)
                .build();
        return build;
    }

    @Builder
    public AuthImageDto(Long id, String origFilename, String filename, String filePath){
        this.id = id;
        this.origFilename = origFilename;
        this.filename = filename;
        this.filePath = filePath;
    }
}