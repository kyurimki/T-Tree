package ttree.it.ttreeGradle.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ttree.it.ttreeGradle.domain.entity.CustomUserDetails;
import ttree.it.ttreeGradle.dto.AuthImageDto;
import ttree.it.ttreeGradle.dto.TeamDto;
import ttree.it.ttreeGradle.dto.UserDto;
import ttree.it.ttreeGradle.service.*;
import ttree.it.ttreeGradle.token.TokenDto;
import ttree.it.ttreeGradle.token.TokenService;
import ttree.it.ttreeGradle.util.MD5Generator;

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
    private TeamService teamService;


    public SignupController(TokenService tokenService, MailService mailService,
                            CustomUserDetailsService customUserDetailsService, AuthImageService authImageService, UserService userService, TeamService teamService) {
        this.tokenService = tokenService;
        this.mailService = mailService;
        this.customUserDetailsService = customUserDetailsService;
        this.authImageService = authImageService;
        this.userService = userService;
        this.teamService = teamService;
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
        status = false;
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
    public String signup(Model model, HttpServletRequest request) {
        if(request.getHeader("REFERER") == null){
            return "SignupEmail";
        } else {
            model.addAttribute("user", new UserDto());
            model.addAttribute("boolStatus", boolIdStatus);
            return "SignupInfo";
        }
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
                userDto.setStudentIdNum(student_id);
                userDto.setRole(identity);
                userDto.setEmail(emailStore);

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = passwordEncoder.encode(userDto.getPassword());
                userDto.setPassword(encodedPassword);

                Long userId = userService.saveUser(userDto);
                boolIdStatus = false;


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
                authImageDto.setImage_id(userId);
                authImageDto.setOrigFilename(origFilename);
                authImageDto.setFilename(filename);
                authImageDto.setFilePath(filePath);

                authImageService.saveAuthImage(authImageDto);

            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        return "login";
    }


    @GetMapping(value = "/user/studentPage")
    public String currentUserDetail(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        String username = customUserDetails.getUsername();
        String usermajor1 = customUserDetails.getMajorOne();
        String usermajor2 = customUserDetails.getMajorTwo();
        String studentIdNum = customUserDetails.getStudentIdNum();
        //System.out.println(username + usermajor1 + usermajor2 + studentIdNum);

        Long userId = customUserDetails.getTeamId();
        try {
            TeamDto teamDto = teamService.getTeam(userId);
            model.addAttribute("teamStatus", teamDto);
        } catch(IllegalArgumentException e) {
            model.addAttribute("teamStatus", null);
        }


        model.addAttribute("username", username);
        model.addAttribute("usermajor1", usermajor1);
        model.addAttribute("usermajor2", usermajor2);
        model.addAttribute("studentIdNum", studentIdNum);



        return "studentPage";
    }

    @RequestMapping(value="/changePW")
    public String changePW() { return "changePW";}

    @PostMapping(value = "/changePW")
    public String changePW(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam("oldpw") String oldPassword, @RequestParam("pass") String password, Model model){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean isChanged = true;

        String dbPassword = customUserDetails.getPassword();
        String studentIdNum = customUserDetails.getStudentIdNum();
        //System.out.println(studentIdNum);

        UserDto userDto = userService.getUserByStudentId(studentIdNum);

        //System.out.println("!" + dbPassword);
        //System.out.println("oldpassword: " + oldPassword);
        //System.out.println("password: " + password);


        if(passwordEncoder.matches(oldPassword, dbPassword)){
            model.addAttribute("isChanged", isChanged);
            String newPassword = passwordEncoder.encode(password);
            userDto.setPassword(newPassword);
            userService.saveUser(userDto);

        }else{
            isChanged = false;
            model.addAttribute("isChanged", isChanged);
        }
       return "changePW";
    }

    @RequestMapping(value="/changeEmail")
    public String changeEmail(){ return "changeEmail"; }

    @PostMapping(value = "/changeEmail")
    public String changeEmail(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                              @RequestParam("oldEmail") String oldEmail, @RequestParam("email") String email, Model model){
        boolean isEmailChanged = true;

        String dbEmail = customUserDetails.getUserEmail();
        String studentIdNum = customUserDetails.getStudentIdNum();

        oldEmail = oldEmail + "@sookmyung.ac.kr";
        email = email + "@sookmyung.ac.kr";

        UserDto userDto = userService.getUserByStudentId(studentIdNum);

        //System.out.println("dbEmail: " + dbEmail);
        //System.out.println("oldEmail: " + oldEmail);
        //System.out.println("email: " + email);

        if (dbEmail.equals(oldEmail)){
            model.addAttribute("isEmailChanged", isEmailChanged);
            userDto.setEmail(email);
            userService.saveUser(userDto);
        } else{
            isEmailChanged = false;
            model.addAttribute("isEmailChanged", isEmailChanged);
        }
        return "changeEmail";
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
