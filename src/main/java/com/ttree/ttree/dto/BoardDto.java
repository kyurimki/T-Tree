package com.ttree.ttree.dto;

import com.ttree.ttree.domain.entity.Board;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDto {
    private Long id;
    private String title;
    private String year;
    private String semester;
    private String content;
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
                .content(content)
                .build();
        return build;
    }

    @Builder
    public BoardDto(Long id, String title, String year, String semester, String content, Long fileId, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.semester = semester;
        this.content = content;
        this.fileId = fileId;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}