package ttree.it.ttreeGradle.dto;

import lombok.*;
import ttree.it.ttreeGradle.domain.entity.Board;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDto {
    private Long id;
    private String title;
    private String year;
    private String semester;

    private String purpose;
    private String content;
    private String effect;
    private int hit;

    private List<String> languages;
    private List<String> types;
    private String langEtc;
    private String typeEtc;
    private String link;

    private Long fileId;
    private String filename;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public Board toEntity() {
        Board build = Board.builder()
                .id(id)
                .title(title)
                .year(year)
                .semester(semester)
                .purpose(purpose)
                .content(content)
                .effect(effect)
                .hit(hit)
                .languages(languages)
                .types(types)
                .langEtc(langEtc)
                .typeEtc(typeEtc)
                .link(link)
                .build();
        return build;
    }

    @Builder
    public BoardDto(Long id, String title, String year, String semester, String purpose, String content, String effect, Long fileId, LocalDateTime createdDate, LocalDateTime modifiedDate, int hit, List<String> languages, List<String> types, String langEtc, String typeEtc, String link) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.semester = semester;
        this.purpose = purpose;
        this.content = content;
        this.effect = effect;
        this.fileId = fileId;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.hit = hit;
        this.languages = languages;
        this.types = types;
        this.langEtc = langEtc;
        this.typeEtc = typeEtc;
        this.link = link;
    }
}