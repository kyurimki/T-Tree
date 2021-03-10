package ttree.it.ttreeGradle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ttree.it.ttreeGradle.dto.MailDto;
import ttree.it.ttreeGradle.service.MailService;


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

    @RequestMapping(value = "/accessDeniedPage")
    public String accessDeniedPage() { return "accessDeniedPage"; }

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