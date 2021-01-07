package com.ttree.ttree.domain.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) /* JPA에게 해당 Entity는 Auditing 기능을 사용함을 알립니다. */
public class Board {

    @Id
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column (nullable = false)
    private String year;

    @Column (nullable = false)
    private String semester;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Builder
    public Board(Long id, String title, String year, String semester, String content) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.semester = semester;
        this.content = content;
    }

    public String getYear() {
        return year;
    }
}