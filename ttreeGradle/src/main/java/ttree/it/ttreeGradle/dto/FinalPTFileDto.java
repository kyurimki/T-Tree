package ttree.it.ttreeGradle.dto;

import lombok.*;
import ttree.it.ttreeGradle.domain.entity.FinalPTFile;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FinalPTFileDto {
    private Long finalPT_id;
    private String finalPT_origFilename;
    private String finalPT_filename;
    private String finalPT_filePath;

    public FinalPTFile toEntity(){
        FinalPTFile build = FinalPTFile.builder()
                .finalPT_id(finalPT_id)
                .finalPT_origFilename(finalPT_origFilename)
                .finalPT_filename(finalPT_filename)
                .finalPT_filePath(finalPT_filePath)
                .build();
        return build;
    }

    @Builder
    public FinalPTFileDto(Long finalPT_id, String finalPT_origFilename, String finalPT_filename, String finalPT_filePath){
        this.finalPT_id = finalPT_id;
        this.finalPT_origFilename = finalPT_origFilename;
        this.finalPT_filename = finalPT_filename;
        this.finalPT_filePath = finalPT_filePath;
    }
}
