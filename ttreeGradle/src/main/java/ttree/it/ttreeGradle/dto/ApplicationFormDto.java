package ttree.it.ttreeGradle.dto;

import lombok.*;
import ttree.it.ttreeGradle.domain.entity.ApplicationForm;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ApplicationFormDto {
    private Long id;
    private String year;
    private String semester;
    private String teamName;
    private String teamMember;
    private String origFilename;
    private String filename;
    private String filePath;

    public ApplicationForm toEntity(){
        ApplicationForm build = ApplicationForm.builder()
                .id(id)
                .year(year)
                .semester(semester)
                .teamName(teamName)
                .teamMember(teamMember)
                .origFilename(origFilename)
                .filename(filename)
                .filePath(filePath)
                .build();
        return build;
    }

    @Builder
    public ApplicationFormDto(Long id, String year, String semester, String teamName, String teamMember, String origFilename, String filename, String filePath){
        this.id = id;
        this.year = year;
        this.semester = semester;
        this.teamName = teamName;
        this.teamMember = teamMember;
        this.origFilename = origFilename;
        this.filename = filename;
        this.filePath = filePath;
    }
}
