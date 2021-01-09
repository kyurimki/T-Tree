package com.ttree.ttree.dto;

import com.ttree.ttree.domain.entity.FairFile;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FairFileDto {
    private Long fair_id;
    private String fair_origFilename;
    private String fair_filename;
    private String fair_filePath;

    public FairFile toEntity(){
        FairFile build = FairFile.builder()
                .fair_id(fair_id)
                .fair_origFilename(fair_origFilename)
                .fair_filename(fair_filename)
                .fair_filePath(fair_filePath)
                .build();
        return build;
    }

    @Builder
    public FairFileDto(Long fair_id, String fair_origFilename, String fair_filename, String fair_filePath){
        this.fair_id = fair_id;
        this.fair_origFilename = fair_origFilename;
        this.fair_filename = fair_filename;
        this.fair_filePath = fair_filePath;
    }

}
