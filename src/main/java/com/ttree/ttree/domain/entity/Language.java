package com.ttree.ttree.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) /* JPA에게 해당 Entity는 Auditing 기능을 사용함을 알립니다. */
public class Language {

    @Column(columnDefinition = "boolean default false")
    private boolean lang_android;

    @Column(columnDefinition = "boolean default false")
    private boolean lang_cpp;

    @Column(columnDefinition = "boolean default false")
    private boolean lang_django;

    @Column(columnDefinition = "boolean default false")
    private boolean lang_html;

    @Column(columnDefinition = "boolean default false")
    private boolean lang_java;

    @Column(columnDefinition = "boolean default false")
    private boolean lang_nodejs;

    @Column(columnDefinition = "boolean default false")
    private boolean lang_python;

    @Column(columnDefinition = "boolean default false")
    private boolean lang_react;

    @Column(columnDefinition = "boolean default false")
    private boolean lang_spring;

    @Column(columnDefinition = "boolean default false")
    private boolean lang_vuejs;

    @Column(columnDefinition = "boolean default false")
    private boolean lang_etc;

    @Id
    private Long board_id;


    @Builder
    public Language(Long board_id, boolean lang_android, boolean lang_cpp, boolean lang_django, boolean lang_html, boolean lang_java,
                    boolean lang_nodejs, boolean lang_python, boolean lang_react, boolean lang_spring, boolean lang_vuejs, boolean lang_etc) {
        this.lang_android = lang_android;
        this.lang_cpp = lang_cpp;
        this.lang_django = lang_django;
        this.lang_html = lang_html;
        this.lang_java = lang_java;
        this.lang_nodejs = lang_nodejs;
        this.lang_python = lang_python;
        this.lang_react = lang_react;
        this.lang_spring = lang_spring;
        this.lang_vuejs = lang_vuejs;
        this.lang_etc = lang_etc;
        this.board_id = board_id;
    }
}