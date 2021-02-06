package com.ttree.ttree.controller;

import com.ttree.ttree.dto.ProjectNotesDto;
import com.ttree.ttree.dto.ProjectRulesDto;
import com.ttree.ttree.service.RuleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RuleController {

    private RuleService ruleService;

    public RuleController(RuleService ruleService){
        this.ruleService = ruleService;
    }

    @RequestMapping(value = "/projectProcess")
    public String projectProcess(){ return "projectProcess";}


    @GetMapping(value = "/projectNotes")
    public String notes(Model model){
        ProjectNotesDto projectNotesDto = ruleService.getProjectNotes(1L);
        model.addAttribute("projectNotes", projectNotesDto);
        //System.out.println(projectNotesDto);
        return "projectNotes";
    }

    @PostMapping(value = "/projectNotes")
    public String saveProjectNotes(ProjectNotesDto projectNotesDto){
        projectNotesDto.setId(1L);
        ruleService.saveProjectNotes(projectNotesDto);
        return "redirect:/projectNotes";
    }

    @GetMapping(value ="/projectNotesSaved")
    public String getProjectNotes(Model model){

        ProjectNotesDto projectNotesDto = ruleService.getProjectNotes(1L);
        model.addAttribute("projectNotes", projectNotesDto);

        return "projectNotesSaved";
    }

    @GetMapping(value = "/projectRules")
    public String rules(Model model){
        ProjectRulesDto projectRulesDto = ruleService.getProjectRules(1L);
        model.addAttribute("projectRules", projectRulesDto);
        return "projectRules";
    }

    @PostMapping(value = "/projectRules")
    public String saveProjectRules(ProjectRulesDto projectRulesDto){
        projectRulesDto.setId(1L);
        ruleService.saveProjectRules(projectRulesDto);
        return "redirect:/projectRules";
    }

    @GetMapping(value = "/projectRulesSaved")
    public String getProjectRules(Model model){
        ProjectRulesDto projectRulesDto = ruleService.getProjectRules(1L);
        model.addAttribute("projectRules", projectRulesDto);

        return "projectRulesSaved";
    }



}
