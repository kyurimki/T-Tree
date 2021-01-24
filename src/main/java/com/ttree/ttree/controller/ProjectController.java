package com.ttree.ttree.controller;

import com.ttree.ttree.dto.ProjectNotesDto;
import com.ttree.ttree.service.ProjectNotesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProjectController {

    private ProjectNotesService projectNotesService;

    public ProjectController(ProjectNotesService projectNotesService){
        this.projectNotesService = projectNotesService;
    }

    @GetMapping(value = "/projectNotes")
    public String notes(Model model){
        ProjectNotesDto projectNotesDto = projectNotesService.getProjectNotes(1L);
        model.addAttribute("projectNotes", projectNotesDto);
        System.out.println(projectNotesDto);
        return "projectNotes";
    }

    @PostMapping(value = "/projectNotes")
    public String saveProjectNotes(ProjectNotesDto projectNotesDto){
        projectNotesDto.setId(1L);
        projectNotesService.saveProjectNotes(projectNotesDto);
        return "redirect:/projectNotes";
    }

    @GetMapping(value ="/projectNotesSaved")
    public String getProjectList(Model model){

        ProjectNotesDto projectNotesDto = projectNotesService.getProjectNotes(1L);
        model.addAttribute("projectNotes", projectNotesDto);


        return "projectNotesSaved";
    }

    @RequestMapping(value = "/projectRules")
    public String projectRules(){ return "projectRules";}

    @RequestMapping(value = "/projectProcess")
    public String projectProcess(){ return "projectProcess";}

}
