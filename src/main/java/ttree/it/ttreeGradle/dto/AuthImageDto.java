package ttree.it.ttreeGradle.dto;

import lombok.*;
import ttree.it.ttreeGradle.domain.entity.AuthImage;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AuthImageDto {
    private Long image_id;
    private String origFilename;
    private String filename;
    private String filePath;

    public AuthImage toEntity(){
        AuthImage build = AuthImage.builder()
                .image_id(image_id)
                .origFilename(origFilename)
                .filename(filename)
                .filePath(filePath)
                .build();
        return build;
    }

    @Builder
    public AuthImageDto(Long image_id, String origFilename, String filename, String filePath){
        this.image_id = image_id;
        this.origFilename = origFilename;
        this.filename = filename;
        this.filePath = filePath;
    }
}