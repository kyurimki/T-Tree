package com.ttree.ttree.controller;

import com.ttree.ttree.domain.entity.CustomUserDetails;
import com.ttree.ttree.domain.entity.User;
import com.ttree.ttree.dto.AuthImageDto;
import com.ttree.ttree.dto.UserDto;
import com.ttree.ttree.service.AuthImageService;
import com.ttree.ttree.service.CustomUserDetailsService;
import com.ttree.ttree.service.UserService;
import com.ttree.ttree.util.MD5Generator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import java.io.File;

@Controller
public class SignupInfoController {

    private CustomUserDetailsService customUserDetailsService;
    private AuthImageService authImageService;
    private UserService userService;

    public SignupInfoController(CustomUserDetailsService customUserDetailsService, AuthImageService authImageService, UserService userService){
        this.customUserDetailsService = customUserDetailsService;
        this.authImageService = authImageService;
        this.userService = userService;
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
            String savePath = System.getProperty("user.dir") + "\\files";
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
        String UserInfo= "";
        String username = customUserDetails.getUsername();
        String usermajor1 = customUserDetails.getMajorOne();
        String usermajor2 = customUserDetails.getMajorTwo();
        String studentIdNum = customUserDetails.getStudentIdNum();

        UserInfo = username + usermajor1 + usermajor2 + studentIdNum;
        System.out.println(UserInfo);
        model.addAttribute("userDetails", UserInfo);

        return "studentPage";
    }

    /*
    @GetMapping("/user/studentPage")
    public String getUserDetail(Model model){
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        Object userDetail = loggedInUser.getPrincipal();

        model.addAttribute("userDetails", userDetail);

        return "studentPage";
    }

    @GetMapping("/user/studentPage")
    public String userDetail(@PathVariable("id") Long id, Model model){
        UserDto userDto = userService.getUser(id);
        System.out.println(userDto);
        model.addAttribute("userDetails", userDto);

        return "studentPage";
    }
     */
}
