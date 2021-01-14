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
    public String emailStore = "";
    public boolean status = false;

    public SignupEmailController(TokenService tokenService, MailService mailService) {
        this.tokenService = tokenService;
        this.mailService = mailService;
    }

    @RequestMapping(value = "/signup/email")
    public String signupEmail(){ return "SignupEmail";}

    @PostMapping(value="/signup/email")
    public void signupEmail(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String code = request.getParameter("code");
        if(code == null) {
            try {
                model.addAttribute("inputEmailID", email);
                email = email + "@sookmyung.ac.kr";
                setEmail(email);

                TokenDto tokenDto = new TokenDto();
                tokenDto.setEmail(email);
                token = UUID.randomUUID().toString();
                tokenDto.setToken(token);
                tokenDto.setCreatedDate(LocalDate.now());
                tokenService.saveToken(tokenDto);

                mailService.mailTokenSend(email, token);
                model.addAttribute("status", status);
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            status = tokenService.checkToken(code);
            model.addAttribute("status", status);
        }
    }

    @GetMapping("/signup/email")
    public String signupEmail(Model model) {
        model.addAttribute("status", status);
        return "SignupEmail";
    }

    public void setEmail(String email) {
        this.emailStore = email;
    }
}
