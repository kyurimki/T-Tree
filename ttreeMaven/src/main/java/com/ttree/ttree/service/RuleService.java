package com.ttree.ttree.service;

import com.ttree.ttree.domain.entity.ProjectNotes;
import com.ttree.ttree.domain.entity.ProjectRules;
import com.ttree.ttree.domain.repository.ProjectNotesRepository;
import com.ttree.ttree.domain.repository.ProjectRulesRepository;
import com.ttree.ttree.dto.ProjectNotesDto;
import com.ttree.ttree.dto.ProjectRulesDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RuleService {
    private ProjectNotesRepository projectNotesRepository;
    private ProjectRulesRepository projectRulesRepository;

    public RuleService(ProjectNotesRepository projectNotesRepository, ProjectRulesRepository projectRulesRepository){
        this.projectNotesRepository = projectNotesRepository;
        this.projectRulesRepository = projectRulesRepository;
    }

    @Transactional
    public Long saveProjectNotes(ProjectNotesDto projectNotesDto){
        //System.out.println("saveProjectList in Service: " + projectNotesDto);
        //System.out.println("saveProjectList toEntity in Service: " + projectNotesDto.toEntity().getContent());
        return projectNotesRepository.save(projectNotesDto.toEntity()).getId();
    }

    @Transactional
    public ProjectNotesDto getProjectNotes(Long id){
        ProjectNotes projectNotes = projectNotesRepository.findById(id).get();

        ProjectNotesDto projectNotesDto = ProjectNotesDto.builder()
                .id(projectNotes.getId())
                .content(projectNotes.getContent())
                .build();

        //System.out.println("getProjectList in Service: " +projectNotesDto);
        return projectNotesDto;
    }

    @Transactional
    public Long saveProjectRules(ProjectRulesDto projectRulesDto){
        return projectRulesRepository.save(projectRulesDto.toEntity()).getId();
    }

    @Transactional
    public ProjectRulesDto getProjectRules(Long id){
        ProjectRules projectRules = projectRulesRepository.findById(id).get();

        ProjectRulesDto projectRulesDto = ProjectRulesDto.builder()
                .id(projectRules.getId())
                .content(projectRules.getContent())
                .build();

        return projectRulesDto;
    }
}