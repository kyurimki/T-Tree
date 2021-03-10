package ttree.it.ttreeGradle.dto;

import lombok.*;
import ttree.it.ttreeGradle.domain.entity.ProjectNotes;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProjectNotesDto {
    private Long id;
    private String content;

    public ProjectNotes toEntity(){
        ProjectNotes build = ProjectNotes.builder()
                .id(id)
                .content(content)
                .build();

        return build;
    }

    @Builder
    public ProjectNotesDto(Long id, String content){
        this.id = id;
        this.content = content;
    }
}