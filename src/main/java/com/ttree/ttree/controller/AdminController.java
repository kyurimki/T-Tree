package com.ttree.ttree.controller;

import com.ttree.ttree.dto.AuthImageDto;
import com.ttree.ttree.dto.UserDto;
import com.ttree.ttree.service.AuthImageService;
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
    public boolean boolIdStatus = false;
    public String student_id = "";

    public AdminController(UserService userService, AuthImageService authImageService) {
        this.userService = userService;
        this.authImageService = authImageService;
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
    public String createUserPage(Model model) {
        model.addAttribute("boolStatus", boolIdStatus);
        return "adminCreateUser";
    }

    @PostMapping("/admin/createUser/checkId")
    public String idCheck(@RequestParam("studentIdNum") String id, Model model) {
        if (id == null) {
            model.addAttribute("id_status", "NULL_ID");
            model.addAttribute("boolStatus", boolIdStatus);
        } else {
            if (userService.idCheck(id)) {
                model.addAttribute("id_status", "SAME_ID");
                model.addAttribute("boolStatus", boolIdStatus);
            } else {
                boolIdStatus = true;
                model.addAttribute("boolStatus", boolIdStatus);
                student_id = id;
                model.addAttribute("id", student_id);
            }
        }
        return "adminCreateUser";
    }

    @PostMapping("/admin/createUser")
    public String createUser(HttpServletRequest request, Model model) {
        if (student_id.equals("")) {
            model.addAttribute("id_status", "INVALID_ID");
        } else {
            try {
                UserDto userDto = new UserDto();
                String studentId = request.getParameter("studentIdNum");
                String name = request.getParameter("studentNum");

                userDto.setStudentIdNum(studentId);
                userDto.setName(name);
                userDto.setRole("ROLE_STUDENT");
                userDto.setEmail("test@sookmyung.ac.kr");
                userDto.setStatus(true);
                userDto.setMajor1("IT공학전공");

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = passwordEncoder.encode("p" + studentId + "!");
                userDto.setPassword(encodedPassword);

                userService.saveUser(userDto);
                model.addAttribute("id_status", "OK");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "adminCreateUser";
    }
}

