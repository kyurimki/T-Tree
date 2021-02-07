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

    @RequestMapping(value = "/alertPage")
    public String alertPage(){ return "alertPage"; }

    @RequestMapping(value = "/user/login")
    public String login(){ return "login"; }

    @GetMapping("/mail")
    public String dispMail() {return "mail";}

    @PostMapping("/mail")
    public void execMail(MailDto mailDto) {
        mailService.mailSend(mailDto);
    }

}