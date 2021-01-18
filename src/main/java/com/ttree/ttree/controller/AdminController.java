package com.ttree.ttree.controller;

import com.ttree.ttree.domain.entity.CustomUserDetails;
import com.ttree.ttree.dto.AuthImageDto;
import com.ttree.ttree.dto.FairFileDto;
import com.ttree.ttree.dto.UserDto;
import com.ttree.ttree.service.AuthImageService;
import com.ttree.ttree.service.CustomUserDetailsService;
import com.ttree.ttree.service.UserService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    public AdminController(UserService userService, AuthImageService authImageService) {
        this.userService = userService;
        this.authImageService = authImageService;
    }

    @GetMapping(value="/admin/userApproval")
    public String userList(Model model) {
        List<UserDto> notApprovedUserList = userService.getNotApprovedUser();
        model.addAttribute("studentList", notApprovedUserList);

        return "adminUserApproval";
    }

    @PostMapping(value="/admin/userApproval")
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
}
