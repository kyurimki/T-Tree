package com.ttree.ttree.domain.entity;

import com.ttree.ttree.util.StringToListConverter;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) /* JPA에게 해당 Entity는 Auditing 기능을 사용함을 알립니다. */
public class Board implements Comparable<Board> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column (nullable = false)
    private String year;

    @Column (nullable = false)
    private String semester;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String purpose; //개발목적

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; //프로그램 설명

    @Column(columnDefinition = "TEXT", nullable = false)
    private String effect; // 기대효과

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column
    private int hit;

    @Column
    @Convert(converter = StringToListConverter.class)
    private List<String> languages;

    @Builder
    public Board(Long id, String title, String year, String semester, String purpose, String content, String effect, int hit, List<String> languages) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.semester = semester;
        this.purpose = purpose;
        this.content = content;
        this.effect = effect;
        this.hit = hit;
        this.languages = languages;
    }

    @Override
    public int compareTo(Board board) {
        return this.id.compareTo(board.id);
    }

    public String getYear() {
        return year;
    }


}