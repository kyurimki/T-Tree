package com.ttree.ttree.controller;

import com.ttree.ttree.domain.entity.CustomUserDetails;
import com.ttree.ttree.dto.AuthImageDto;
import com.ttree.ttree.dto.UserDto;
import com.ttree.ttree.service.AuthImageService;
import com.ttree.ttree.service.CustomUserDetailsService;
import com.ttree.ttree.service.MailService;
import com.ttree.ttree.service.UserService;
import com.ttree.ttree.token.TokenDto;
import com.ttree.ttree.token.TokenService;
import com.ttree.ttree.util.MD5Generator;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDate;
import java.util.UUID;

@Controller
public class SignupController {
    public TokenService tokenService;
    private MailService mailService;
    public String token;
    public String emailStore = "";
    public boolean status = false;

    private CustomUserDetailsService customUserDetailsService;
    private AuthImageService authImageService;
    private UserService userService;


    public SignupController(TokenService tokenService, MailService mailService,
                            CustomUserDetailsService customUserDetailsService, AuthImageService authImageService, UserService userService) {
        this.tokenService = tokenService;
        this.mailService = mailService;
        this.customUserDetailsService = customUserDetailsService;
        this.authImageService = authImageService;
        this.userService = userService;
    }

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
    public String signupEmailStatus(Model model) {
        model.addAttribute("status", status);
        return "SignupEmail";
    }

    public void setEmail(String email) {
        this.emailStore = email;
    }


    @RequestMapping(value = "/signup/info")
    public String signup(Model model){
        model.addAttribute("user", new UserDto());
        return "SignupInfo";
    }

    @PostMapping("/process_register")
    public String processRegister(UserDto userDto, @RequestParam("authImg") MultipartFile files) {
        try {
            String origFilename = files.getOriginalFilename();
            String filename = new MD5Generator(origFilename).toString();
            /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
            String savePath = System.getProperty("user.dir") + "/files";
            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
            if (!new File(savePath).exists()) {
                try {
                    new File(savePath).mkdir();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
            String filePath = savePath + "\\" + filename;
            files.transferTo(new File(filePath));

            AuthImageDto authImageDto = new AuthImageDto();
            authImageDto.setOrigFilename(origFilename);
            authImageDto.setFilename(filename);
            authImageDto.setFilePath(filePath);

            Long fileId = authImageService.saveAuthImage(authImageDto);
            userDto.setId(fileId);

            userDto.setEmail(emailStore);

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(userDto.getPassword());

            userDto.setPassword(encodedPassword);
            userService.saveUser(userDto);

        }catch(Exception e) {
            e.printStackTrace();
        }

        return "login";
    }

    @RequestMapping(value ="/user/studentPage")
    public String studentPage(){ return "studentPage"; }

    @GetMapping(value = "/user/studentPage")
    public String currentUserDetail(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        String username = customUserDetails.getUsername();
        String usermajor1 = customUserDetails.getMajorOne();
        String usermajor2 = customUserDetails.getMajorTwo();
        String studentIdNum = customUserDetails.getStudentIdNum();
        //System.out.println(username + usermajor1 + usermajor2 + studentIdNum);

        model.addAttribute("username", username);
        model.addAttribute("usermajor1", usermajor1);
        model.addAttribute("usermajor2", usermajor2);
        model.addAttribute("studentIdNum", studentIdNum);

        return "studentPage";
    }

}
