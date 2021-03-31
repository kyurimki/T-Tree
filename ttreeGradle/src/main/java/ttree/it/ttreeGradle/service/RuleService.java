package ttree.it.ttreeGradle.service;

import org.springframework.stereotype.Service;
import ttree.it.ttreeGradle.domain.entity.ProjectNotes;
import ttree.it.ttreeGradle.domain.entity.ProjectRules;
import ttree.it.ttreeGradle.domain.repository.ProjectNotesRepository;
import ttree.it.ttreeGradle.domain.repository.ProjectRulesRepository;
import ttree.it.ttreeGradle.dto.ProjectNotesDto;
import ttree.it.ttreeGradle.dto.ProjectRulesDto;

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