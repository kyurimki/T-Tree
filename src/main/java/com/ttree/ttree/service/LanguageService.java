package com.ttree.ttree.service;

import com.ttree.ttree.domain.entity.Language;
import com.ttree.ttree.domain.repository.LanguageRepository;
import com.ttree.ttree.dto.LanguageDto;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class LanguageService {
    private LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Transactional
    public void saveLanguage(LanguageDto languageDto) {
        languageRepository.save(languageDto.toEntity());
    }
    @Transactional
    public List<LanguageDto> getLanguageList() {
        List<Language> languageList = languageRepository.findAll();
        List<LanguageDto> languageDtoList = new ArrayList<>();

        for(Language language : languageList) {
            LanguageDto languageDto = LanguageDto.builder()
                    .lang_android(language.isLang_android())
                    .lang_cpp(language.isLang_cpp())
                    .lang_django(language.isLang_django())
                    .lang_html(language.isLang_html())
                    .lang_java(language.isLang_java())
                    .lang_nodejs(language.isLang_nodejs())
                    .lang_python(language.isLang_python())
                    .lang_react(language.isLang_react())
                    .lang_spring(language.isLang_spring())
                    .lang_vuejs(language.isLang_vuejs())
                    .lang_etc(language.getLang_etc())
                    .board_id(language.getBoard_id())
                    .build();
            languageDtoList.add(languageDto);
        }
        return languageDtoList;
    }
    @Transactional
    public LanguageDto getLanguage(Long id) {
        Language language = languageRepository.findById(id).get();

        LanguageDto languageDto = LanguageDto.builder()
                .lang_android(language.isLang_android())
                .lang_cpp(language.isLang_cpp())
                .lang_django(language.isLang_django())
                .lang_html(language.isLang_html())
                .lang_java(language.isLang_java())
                .lang_nodejs(language.isLang_nodejs())
                .lang_python(language.isLang_python())
                .lang_react(language.isLang_react())
                .lang_spring(language.isLang_spring())
                .lang_vuejs(language.isLang_vuejs())
                .lang_etc(language.getLang_etc())
                .board_id(language.getBoard_id())
                .build();
        return languageDto;
    }

    @Transactional
    public String getLangList(Long id){

        LanguageDto languageDto = getLanguage(id);
        String langList = "";

        if(languageDto.isLang_android()){
            langList += "Android ";
        }
        if (languageDto.isLang_cpp()){
            langList += "C/C++ ";
        }
        if(languageDto.isLang_django()){
            langList += "Django ";
        }
        if(languageDto.isLang_html()){
            langList += "HTML/CSS/JS ";
        }
        if(languageDto.isLang_java()){
            langList += "Java ";
        }
        if(languageDto.isLang_nodejs()){
            langList += "NodeJS ";
        }
        if(languageDto.isLang_python()){
            langList += "Python ";
        }
        if(languageDto.isLang_react()){
            langList += "React-Native ";
        }
        if(languageDto.isLang_spring()){
            langList += "Spring ";
        }
        if(languageDto.isLang_vuejs()){
            langList += "VueJS ";
        }
        if(languageDto.getLang_etc() != null){
            langList += languageDto.getLang_etc();
        }
        return langList;
    }

    @Transactional
    public void deleteLanguage(Long id) {
        languageRepository.deleteById(id);
    }
}
