package ttree.it.ttreeGradle.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ttree.it.ttreeGradle.domain.entity.CustomUserDetails;
import ttree.it.ttreeGradle.dto.*;
import ttree.it.ttreeGradle.service.ApplicationFormService;
import ttree.it.ttreeGradle.service.RuleService;
import ttree.it.ttreeGradle.util.MD5Generator;

import java.io.File;

@Controller
public class RuleController {

    private RuleService ruleService;
    private ApplicationFormService applicationFormService;

    public RuleController(RuleService ruleService, ApplicationFormService applicationFormService){
        this.ruleService = ruleService;
        this.applicationFormService = applicationFormService;
    }

    @RequestMapping(value = "/projectProcess")
    public String projectProcess(){ return "projectProcess";}

    @GetMapping(value = "/projectProcess/upload")
    public String submitApplicationForm(){
        return "uploadAppliform";
    }

    @PostMapping(value = "/projectProcess/upload")
    public String postApplicationForm(@RequestParam("applicationFile") MultipartFile applicationFile, ApplicationFormDto applicationFormDto){
        try {
            if (!applicationFile.isEmpty()) {
                String origApplicationFilename = applicationFile.getOriginalFilename();
                String applicationFilename = new MD5Generator(origApplicationFilename).toString();
                String saveApplicationPath = System.getProperty("user.dir") + "/paperFiles";
                if (!new File(saveApplicationPath).exists()) {
                    try {
                        new File(saveApplicationPath).mkdir();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
                String applicationFilePath = saveApplicationPath + "/" + applicationFilename;
                applicationFile.transferTo(new File(applicationFilePath));

                applicationFormDto.setOrigFilename(origApplicationFilename);
                applicationFormDto.setFilename(applicationFilename);
                applicationFormDto.setFilePath(applicationFilePath);

                applicationFormService.saveApplicationForm(applicationFormDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "projectProcess";
    }


    @GetMapping(value = "/projectNotes")
    public String notes(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        if(customUserDetails.getUserStatus()) {
            ProjectNotesDto projectNotesDto = ruleService.getProjectNotes(1L);
            model.addAttribute("projectNotes", projectNotesDto);
            //System.out.println(projectNotesDto);
            return "projectNotes";
        }else{
            return "alertPage";
        }
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
    public String rules(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        if(customUserDetails.getUserStatus()) {
            ProjectRulesDto projectRulesDto = ruleService.getProjectRules(1L);
            model.addAttribute("projectRules", projectRulesDto);
            return "projectRules";
        }else{
            return "alertPage";
        }
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
