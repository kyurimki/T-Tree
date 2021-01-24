package com.ttree.ttree.service;

import com.ttree.ttree.domain.entity.ProjectNotes;
import com.ttree.ttree.domain.repository.ProjectNotesRepository;
import com.ttree.ttree.dto.ProjectNotesDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ProjectNotesService {
    private ProjectNotesRepository projectNotesRepository;

    public ProjectNotesService(ProjectNotesRepository projectNotesRepository){
        this.projectNotesRepository = projectNotesRepository;
    }

    @Transactional
    public Long saveProjectNotes(ProjectNotesDto projectNotesDto){
        System.out.println("saveProjectList in Service: " + projectNotesDto);
        System.out.println("saveProjectList toEntity in Service: " + projectNotesDto.toEntity().getContent());
        return projectNotesRepository.save(projectNotesDto.toEntity()).getId();
    }

    @Transactional
    public ProjectNotesDto getProjectNotes(Long id){

        ProjectNotes projectNotes = projectNotesRepository.findById(id).get();

        ProjectNotesDto projectNotesDto = ProjectNotesDto.builder()
                .id(projectNotes.getId())
                .content(projectNotes.getContent())
                .build();

        System.out.println("getProjectList in Service: " +projectNotesDto);
        return projectNotesDto;
    }
}