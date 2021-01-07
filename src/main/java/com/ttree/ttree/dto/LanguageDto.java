package com.ttree.ttree.dto;

import com.ttree.ttree.domain.entity.Language;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LanguageDto {
    private Long lang_id;
    private boolean lang_android;
    private boolean lang_cpp;
    private boolean lang_django;
    private boolean lang_html;
    private boolean lang_java;
    private boolean lang_nodejs;
    private boolean lang_python;
    private boolean lang_react;
    private boolean lang_spring;
    private boolean lang_vuejs;
    private boolean lang_etc;
    private Long board_id;

    public Language toEntity() {
        Language build = Language.builder()
                .lang_android(lang_android)
                .lang_cpp(lang_cpp)
                .lang_django(lang_django)
                .lang_html(lang_html)
                .lang_java(lang_java)
                .lang_nodejs(lang_nodejs)
                .lang_python(lang_python)
                .lang_react(lang_react)
                .lang_spring(lang_spring)
                .lang_vuejs(lang_vuejs)
                .lang_etc(lang_etc)
                .board_id(board_id)
                .build();
        return build;
    }

    @Builder
    public LanguageDto(Long board_id, boolean lang_android, boolean lang_cpp, boolean lang_django, boolean lang_html, boolean lang_java,
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
