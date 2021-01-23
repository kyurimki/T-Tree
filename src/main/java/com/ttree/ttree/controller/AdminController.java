package com.ttree.ttree.controller;

import com.ttree.ttree.dto.AuthImageDto;
import com.ttree.ttree.dto.TeamDto;
import com.ttree.ttree.dto.UserDto;
import com.ttree.ttree.service.AuthImageService;
import com.ttree.ttree.service.TeamService;
import com.ttree.ttree.service.UserService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class AdminController {
    private UserService userService;
    private AuthImageService authImageService;
    private TeamService teamService;
    public String student_id = "";
    public UserDto userDto;
    boolean signupRecord = false;

    public AdminController(UserService userService, AuthImageService authImageService, TeamService teamService) {
        this.userService = userService;
        this.authImageService = authImageService;
        this.teamService = teamService;
    }

    @GetMapping(value = "/admin/userApproval")
    public String userList(Model model) {
        List<UserDto> notApprovedUserList = userService.getNotApprovedUser();
        model.addAttribute("studentList", notApprovedUserList);

        return "adminUserApproval";
    }

    @PostMapping(value = "/admin/userApproval")
    public String userApproval(HttpServletRequest request) {
        String id = request.getParameter("id");
        UserDto userDto = userService.getUser(Long.valueOf(id));
        userDto.setStatus(true);
        userService.saveUser(userDto);
        return "redirect:/admin/userApproval";
    }

    @GetMapping("/admin/userApproval/{id}")
    public ResponseEntity<Resource> fairFileDownload(@PathVariable("id") Long fileId) throws IOException {
        AuthImageDto authImageDto = authImageService.getAuthImage(fileId);
        Path authImgPath = Paths.get(authImageDto.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(authImgPath));
        String authImgFilename = authImageDto.getOrigFilename();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(authImgFilename, "utf-8") + "\"")
                .body(resource);
    }

    @GetMapping("/admin/createUser")
    public String createUserPage() {
        return "adminCreateUser";
    }

    @PostMapping("/admin/createUser/checkId")
    public String idCheck(@RequestParam("studentIdNum") String id, Model model) {
        if (id == null) {
            model.addAttribute("id_status", "NULL_ID");
        } else {
            student_id = id;
            model.addAttribute("studentId", student_id);
            if(userService.getUserByStudentId(id) != null) {
                signupRecord = true;
                userDto = userService.getUserByStudentId(id);
                model.addAttribute("userInfo", userDto);
                if(userDto.getTeamIdNum() != null) {
                    Long teamId = userDto.getTeamIdNum();
                    TeamDto teamDto = teamService.getTeam(teamId);
                    model.addAttribute("teamInfo", teamDto);
                }
            }
        }
        return "adminCreateUser";
    }

    @PostMapping("/admin/createUser")
    public String createUser(HttpServletRequest request, Model model) {
        String name = request.getParameter("studentName");
        if (name == null) {
            model.addAttribute("id_status", "INVALID_ID");
        } else {
            String email = request.getParameter("studentEmail");
            String phoneNum = request.getParameter("studentPhoneNum");
            try {
                if(!signupRecord) {
                    userDto = new UserDto();

                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    String encodedPassword = passwordEncoder.encode("p" + student_id + "!");
                    userDto.setPassword(encodedPassword);
                    userDto.setMajor1("IT공학전공");
                } else {
                    userDto.setPassword(userDto.getPassword());
                    userDto.setMajor1(userDto.getMajor1());
                    userDto.setMajor2(userDto.getMajor2());
                }
                userDto.setStudentIdNum(student_id);
                userDto.setName(name);
                userDto.setRole("ROLE_STUDENT");
                userDto.setEmail(email);
                userDto.setPhoneNum(phoneNum);
                userDto.setStatus(true);

                userService.saveUser(userDto);
                model.addAttribute("id_status", "OK");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "adminCreateUser";
    }
}

