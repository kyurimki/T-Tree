package com.ttree.ttree.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
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
    public boolean boolIdStatus = false;
    public String student_id = "";


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

    @PostMapping(value="/signup/email/getEmail")
    public String getEmail(HttpServletRequest request, Model model) {
        emailStore = request.getParameter("email");
        try {
            model.addAttribute("inputEmailID", emailStore);
            emailStore = emailStore + "@sookmyung.ac.kr";
            setEmail(emailStore);

            TokenDto tokenDto = new TokenDto();
            tokenDto.setEmail(emailStore);
            token = UUID.randomUUID().toString();
            tokenDto.setToken(token);
            tokenDto.setCreatedDate(LocalDate.now());
            tokenService.saveToken(tokenDto);

            mailService.mailTokenSend(emailStore, token);
            model.addAttribute("status", status);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "SignupEmail";
    }

    @PostMapping(value="/signup/email")
    public String signupEmail(HttpServletRequest request, Model model) {
        String code = request.getParameter("code");
        status = tokenService.checkToken(emailStore, code);
        model.addAttribute("status", status);
        return "SignupEmail";
    }

    @GetMapping("/signup/email")
    public String signupEmailStatus(Model model) {
        model.addAttribute("status", status);
        return "SignupEmail";
    }

    public void setEmail(String email) {
        this.emailStore = email;
    }

    @GetMapping(value = "/signup/info")
    public String signup(Model model) {
        model.addAttribute("user", new UserDto());
        model.addAttribute("boolStatus", boolIdStatus);
        model.addAttribute("id_status", "!");
        return "SignupInfo";
    }

    @PostMapping(value="/signup/info/idCheck")
    public String idCheck(@RequestParam("studentIdNum") String id, Model model) {
        if(id == null) {
            model.addAttribute("id_status", "NULL_ID");
            model.addAttribute("boolStatus", boolIdStatus);
        } else {
            if(userService.idCheck(id)) {
                model.addAttribute("id_status", "SAME_ID");
                model.addAttribute("boolStatus", boolIdStatus);
            } else {
                boolIdStatus = true;
                model.addAttribute("boolStatus", boolIdStatus);
                student_id = id;
                model.addAttribute("id", student_id);
            }
        }
        return "SignupInfo";
    }

   @PostMapping("/signup/info")
   public String processRegister(UserDto userDto, @RequestParam("identity") String identity, @RequestParam("authImg") MultipartFile files, Model model) {
        if(student_id.equals("")) {
            model.addAttribute("id_status", "INVALID_ID");
        } else {
            try {
                String origFilename = files.getOriginalFilename();
                String filename = new MD5Generator(origFilename).toString();
                String savePath = System.getProperty("user.dir") + "/files";
                if (!new File(savePath).exists()) {
                    try {
                        new File(savePath).mkdir();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
                String filePath = savePath + "/" + filename;
                files.transferTo(new File(filePath));

                AuthImageDto authImageDto = new AuthImageDto();
                authImageDto.setOrigFilename(origFilename);
                authImageDto.setFilename(filename);
                authImageDto.setFilePath(filePath);

                Long fileId = authImageService.saveAuthImage(authImageDto);
                userDto.setId(fileId);
                userDto.setStudentIdNum(student_id);
                userDto.setRole(identity);
                userDto.setEmail(emailStore);

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = passwordEncoder.encode(userDto.getPassword());
                userDto.setPassword(encodedPassword);

                userService.saveUser(userDto);
                boolIdStatus = false;
            }catch(Exception e) {
                e.printStackTrace();
            }
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

    @PostMapping(value = "/user/studentPage")
    public String changePW(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam("oldpw") String oldPassword, @RequestParam("pass") String password, Model model){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean isChanged = true;

        String dbPassword = customUserDetails.getPassword();
        String studentIdNum = customUserDetails.getStudentIdNum();
        System.out.println(studentIdNum);

        UserDto userDto = userService.getUserByStudentId(studentIdNum);

        System.out.println("!" + dbPassword);
        System.out.println("oldpassword: " + oldPassword);
        System.out.println("password: " + password);


        if(passwordEncoder.matches(oldPassword, dbPassword)){
            model.addAttribute("isChanged", isChanged);
            String newPassword = passwordEncoder.encode(password);
            userDto.setPassword(newPassword);
            userService.saveUser(userDto);

        }else{
            isChanged = false;
            model.addAttribute("isChanged", isChanged);
        }

       return "studentPage";
    }

    @GetMapping(value = "/login/findPW")
    public String findPW() {
        return "FindPW";
    }

    @PostMapping(value = "/login/findPW")
    public String findPW(@RequestParam("id") String studentId, Model model){
        UserDto userDto = userService.getUserByStudentId(studentId);
        boolean infoExist = false;
        if(userDto != null) {
            String password = randomPWGenerator();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(password);

            userDto.setPassword(encodedPassword);
            userService.saveUser(userDto);

            String email = userDto.getEmail();

            mailService.mailPasswordSend(email, password);
            infoExist = true;
        }
        model.addAttribute("exist", infoExist);
        return "FindPW";
    }

    public String randomPWGenerator() {
        String generatedPW = UUID.randomUUID().toString();
        generatedPW = generatedPW.replaceAll("-", "");
        generatedPW = generatedPW.substring(0, 16);
        return generatedPW;
    }
}
