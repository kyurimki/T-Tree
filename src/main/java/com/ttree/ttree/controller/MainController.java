package com.ttree.ttree.controller;

import com.ttree.ttree.dto.MailDto;
import com.ttree.ttree.service.MailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    private final MailService mailService;

    public MainController(MailService mailService) {
        this.mailService = mailService;
    }

    @RequestMapping(value = "/")
    public String home(){
        return "index";
    }

    @RequestMapping(value = "/user/login")
    public String login(){ return "login"; }

    @RequestMapping(value = "/projectNotes")
    public String projectNotes(){
        return "projectNotes";
    }

    @RequestMapping(value = "/projectRules")
    public String projectRules(){ return "projectRules";}

    @RequestMapping(value = "/projectProcess")
    public String projectProcess(){ return "projectProcess";}

    @RequestMapping(value = "/staff/staffPage")
    public String staffPage(){ return "staffPage"; }


    @RequestMapping(value = "/login/findPW")
    public String findPW(){ return "FindPW";}

    @GetMapping("/mail")
    public String dispMail() {return "mail";}

    @PostMapping("/mail")
    public void execMail(MailDto mailDto) {
        mailService.mailSend(mailDto);
    }
}