package ttree.it.ttreeGradle.dto;

import lombok.*;
import ttree.it.ttreeGradle.domain.entity.ProjectRules;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProjectRulesDto {
    private Long id;
    private String content;

    public ProjectRules toEntity(){
        ProjectRules build = ProjectRules.builder()
                .id(id)
                .content(content)
                .build();

        return build;
    }

    @Builder
    public ProjectRulesDto(Long id, String content){
        this.id = id;
        this.content = content;
    }
}
