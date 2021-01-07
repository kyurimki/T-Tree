package com.ttree.ttree.controller;

import com.ttree.ttree.service.MailService;
import com.ttree.ttree.token.TokenDto;
import com.ttree.ttree.token.TokenService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.UUID;

@Controller
public class SignupEmailController {
    public TokenService tokenService;
    private MailService mailService;
    public String token;
    public String email = "";
    public boolean status = false;

    public SignupEmailController(TokenService tokenService, MailService mailService) {
        this.tokenService = tokenService;
        this.mailService = mailService;
    }

    @RequestMapping(value = "/signupEmail")
    public String signupEmail(){ return "SignupEmail";}

    @PostMapping(value="/signupEmail")
    public void signupEmail(HttpServletRequest request) {
        email = request.getParameter("email");
        String code = request.getParameter("code");
        if(code == null) {
            try {
                email = email + "@sookmyung.ac.kr";

                TokenDto tokenDto = new TokenDto();
                tokenDto.setEmail(email);
                token = UUID.randomUUID().toString();
                tokenDto.setToken(token);
                tokenDto.setCreatedDate(LocalDate.now());
                tokenService.saveToken(tokenDto);

                mailService.mailTokenSend(email, token);
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else if(email == null) {
            status = tokenService.checkToken(code);
        }

    }

    @GetMapping("/signupEmail")
    public String signupEmail(Model model) {
        model.addAttribute("status", status);
        model.addAttribute("email", email);
        return "SignupEmail";
    }
}
