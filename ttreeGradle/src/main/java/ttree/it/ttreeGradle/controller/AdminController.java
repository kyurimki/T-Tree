package ttree.it.ttreeGradle.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ttree.it.ttreeGradle.domain.entity.CustomUserDetails;
import ttree.it.ttreeGradle.dto.*;
import ttree.it.ttreeGradle.service.ApplicationFormService;
import ttree.it.ttreeGradle.service.AuthImageService;
import ttree.it.ttreeGradle.service.TeamService;
import ttree.it.ttreeGradle.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {
    private UserService userService;
    private AuthImageService authImageService;
    private TeamService teamService;
    private ApplicationFormService applicationFormService;

    public String student_id = "";
    public UserDto userDto;
    boolean signupRecord = false;
    public boolean idCheckStatus = false;



    @GetMapping(value = "/admin/adminPage")
    public String adminPage(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        String username = customUserDetails.getUsername();
        String usermajor1 = customUserDetails.getMajorOne();
        String usermajor2 = customUserDetails.getMajorTwo();
        String studentIdNum = customUserDetails.getStudentIdNum();

        model.addAttribute("username", username);
        model.addAttribute("usermajor1", usermajor1);
        model.addAttribute("usermajor2", usermajor2);
        model.addAttribute("studentIdNum", studentIdNum);

        return "adminPage";
    }

    public AdminController(UserService userService, AuthImageService authImageService, TeamService teamService, ApplicationFormService applicationFormService) {
        this.userService = userService;
        this.authImageService = authImageService;
        this.teamService = teamService;
        this.applicationFormService = applicationFormService;
    }

    @GetMapping(value = "/admin/userApproval")
    public String userList(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        if(customUserDetails.getUserStatus()) {
            List<UserDto> notApprovedUserList = userService.getNotApprovedUser();
            model.addAttribute("studentList", notApprovedUserList);

            return "adminUserApproval";
        }else{
            return "alertPage";
        }
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
    public String createUserPage(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        if(customUserDetails.getUserStatus()) {
            model.addAttribute("idCheckStatus", false);
            return "adminCreateUser";
        }else{
            return "alertPage";
        }
    }

    @PostMapping("/admin/createUser/checkId")
    public String idCheck(@RequestParam("studentIdNum") String id, Model model) {
        student_id = id;
        model.addAttribute("studentId", student_id);
        if(userService.getUserByStudentInfo(0, id).size() != 0) {
            signupRecord = true;
            userDto = userService.getUserByStudentInfo(0, id).get(0);
            model.addAttribute("userInfo", userDto);
            if(userDto.getTeamIdNum() != null) {
                Long teamId = userDto.getTeamIdNum();
                TeamDto teamDto = teamService.getTeam(teamId);
                model.addAttribute("teamInfo", teamDto);
            }
            idCheckStatus = true;
            model.addAttribute("idCheckStatus", idCheckStatus);
            idCheckStatus = false;
        }
        return "adminCreateUser";
    }

    @PostMapping("/admin/createUser")
    public String createUser(HttpServletRequest request, Model model) {
        String name = request.getParameter("studentName");
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
                userDto.setTeamIdNum(userDto.getTeamIdNum());
            }
            userDto.setStudentIdNum(student_id);
            userDto.setName(name);
            userDto.setRole("ROLE_STUDENT");
            userDto.setEmail(email);
            userDto.setPhoneNum(phoneNum);
            userDto.setStatus(true);

            String teamName = request.getParameter("teamName");
            String teamYear = request.getParameter("teamYear");
            String teamSemester = request.getParameter("teamSemester");

            List<TeamDto> teamDtoList = teamService.getTeamListByName(teamName);
            TeamDto teamDto;
            boolean flag = false;
            if(!teamDtoList.isEmpty()) {
                for(TeamDto team : teamDtoList) {
                    if(team.getTeamYear().equals(teamYear) && team.getTeamSemester().equals(teamSemester)) {
                        userDto.setTeamIdNum(team.getTeamId());
                        flag = true;
                        break;
                    }
                }
            }
            if(!flag) {
                teamDto = new TeamDto();
                teamDto.setTeamName(teamName);
                teamDto.setTeamYear(teamYear);
                teamDto.setTeamSemester(teamSemester);
                Long teamId = teamService.saveTeam(teamDto);
                userDto.setTeamIdNum(teamId);
            }
            userService.saveUser(userDto);
            signupRecord = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "adminCreateUser";
    }

    @GetMapping(value = "/admin/updateStatus")
    public String getTeamList(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        if(customUserDetails.getUserStatus()) {
            List<TeamDto> teamList = teamService.getTeamList();
            model.addAttribute("teamList", teamList);
            return "adminUpdateStatus";
        }else{
            return "alertPage";
        }
    }

    @GetMapping("/admin/updateStatus/search")
    public String redirectToUpdateStatus() {
        return "redirect:/admin/updateStatus";
    }

    @PostMapping("/admin/updateStatus/search")
    public String searchTeam(HttpServletRequest request, Model model) {
        String yearToSearch = request.getParameter("teamYear");
        String semesterToSearch = request.getParameter("teamSemester");
        if(yearToSearch.equals("")) {
            if(!semesterToSearch.equals("")) {
                model.addAttribute("ERROR", "올바르게 선택되지 않았습니다.");
            }
            List<TeamDto> teamList = teamService.getTeamList();
            model.addAttribute("teamList", teamList);
        } else {
            List<TeamDto> teamList = teamService.searchTeamByTime(yearToSearch, semesterToSearch);
            model.addAttribute("teamList", teamList);
            model.addAttribute("yearSearched", yearToSearch);
            model.addAttribute("semSearched", semesterToSearch);
        }
        return "adminUpdateStatus";
    }

    @GetMapping("/admin/updateStatus/status1/{id}")
    public String updateStatus1(@PathVariable("id") Long id, Model model) {
        TeamDto teamDto = teamService.getTeam(id);
        model.addAttribute("teamId", id.toString());
        model.addAttribute("teamInfo", teamDto);
        model.addAttribute("statusStage", "1");
        model.addAttribute("status", false);
        return "adminUpdateStatusWindow";
    }

    @PostMapping("/admin/updateStatus/status1/{id}")
    public String storeStatus1(@PathVariable("id") Long id, HttpServletRequest request, Model model) {
        TeamDto teamDto = teamService.getTeam(id);
        String status = request.getParameter("status");
        String reason = "";
        LocalDate today = LocalDate.now();

        if(status.equals("탈락")) {
            reason = request.getParameter("reason");
            teamDto.setTeamStatus1("["+today+"] "+status+": " +reason);
        } else {
            teamDto.setTeamStatus1("["+today+"] "+status);
        }
        teamDto.setTeamName(teamDto.getTeamName());
        teamDto.setTeamYear(teamDto.getTeamYear());
        teamDto.setTeamSemester(teamDto.getTeamSemester());
        teamService.saveTeam(teamDto);

        model.addAttribute("teamInfo", teamDto);
        model.addAttribute("statusStage", "1");
        model.addAttribute("status", "end");

        return "adminUpdateStatusWindow";
    }

    @GetMapping("/admin/updateStatus/status2/{id}")
    public String updateStatus2(@PathVariable("id") Long id, Model model) {
        TeamDto teamDto = teamService.getTeam(id);
        model.addAttribute("teamId", id.toString());
        model.addAttribute("teamInfo", teamDto);
        model.addAttribute("statusStage", "2");
        model.addAttribute("status", false);
        return "adminUpdateStatusWindow";
    }

    @PostMapping("/admin/updateStatus/status2/{id}")
    public String storeStatus2(@PathVariable("id") Long id, HttpServletRequest request, Model model) {
        TeamDto teamDto = teamService.getTeam(id);
        String status = request.getParameter("status");
        String reason = "";
        LocalDate today = LocalDate.now();

        if(status.equals("탈락")) {
            reason = request.getParameter("reason");
            teamDto.setTeamStatus2("["+today+"] "+status+": " +reason);
        } else {
            teamDto.setTeamStatus2("["+today+"] "+status);
        }
        teamDto.setTeamName(teamDto.getTeamName());
        teamDto.setTeamYear(teamDto.getTeamYear());
        teamDto.setTeamSemester(teamDto.getTeamSemester());
        teamService.saveTeam(teamDto);

        model.addAttribute("teamInfo", teamDto);
        model.addAttribute("statusStage", "2");
        model.addAttribute("status", "end");

        return "adminUpdateStatusWindow";
    }

    @GetMapping("/admin/updateStatus/status3/{id}")
    public String updateStatus3(@PathVariable("id") Long id, Model model) {
        TeamDto teamDto = teamService.getTeam(id);
        model.addAttribute("teamId", id.toString());
        model.addAttribute("teamInfo", teamDto);
        model.addAttribute("statusStage", "3");
        model.addAttribute("status", false);
        return "adminUpdateStatusWindow";
    }

    @PostMapping("/admin/updateStatus/status3/{id}")
    public String storeStatus3(@PathVariable("id") Long id, HttpServletRequest request, Model model) {
        TeamDto teamDto = teamService.getTeam(id);
        String status = request.getParameter("status");
        String reason = "";
        LocalDate today = LocalDate.now();

        if(status.equals("탈락")) {
            reason = request.getParameter("reason");
            teamDto.setTeamStatus3("["+today+"] "+status+": " +reason);
        } else {
            teamDto.setTeamStatus3("["+today+"] "+status);
        }
        teamDto.setTeamName(teamDto.getTeamName());
        teamDto.setTeamYear(teamDto.getTeamYear());
        teamDto.setTeamSemester(teamDto.getTeamSemester());
        teamService.saveTeam(teamDto);

        model.addAttribute("teamInfo", teamDto);
        model.addAttribute("statusStage", "3");
        model.addAttribute("status", "end");

        return "adminUpdateStatusWindow";
    }

    @GetMapping("/admin/updateStatus/status4/{id}")
    public String updateStatus4(@PathVariable("id") Long id, Model model) {
        TeamDto teamDto = teamService.getTeam(id);
        model.addAttribute("teamId", id.toString());
        model.addAttribute("teamInfo", teamDto);
        model.addAttribute("statusStage", "4");
        model.addAttribute("status", false);
        return "adminUpdateStatusWindow";
    }

    @PostMapping("/admin/updateStatus/status4/{id}")
    public String storeStatus4(@PathVariable("id") Long id, HttpServletRequest request, Model model) {
        TeamDto teamDto = teamService.getTeam(id);
        String status = request.getParameter("status");
        String reason = "";
        LocalDate today = LocalDate.now();

        if(status.equals("탈락")) {
            reason = request.getParameter("reason");
            teamDto.setTeamStatus4("["+today+"] "+status+": " +reason);
        } else {
            teamDto.setTeamStatus4("["+today+"] "+status);
        }
        teamDto.setTeamName(teamDto.getTeamName());
        teamDto.setTeamYear(teamDto.getTeamYear());
        teamDto.setTeamSemester(teamDto.getTeamSemester());
        teamService.saveTeam(teamDto);

        model.addAttribute("teamInfo", teamDto);
        model.addAttribute("statusStage", "4");
        model.addAttribute("status", "end");

        return "adminUpdateStatusWindow";
    }

    @GetMapping("/admin/updateStatus/statusFinal/{id}")
    public String updateStatusFinal(@PathVariable("id") Long id, Model model) {
        TeamDto teamDto = teamService.getTeam(id);
        model.addAttribute("teamId", id.toString());
        model.addAttribute("teamInfo", teamDto);
        model.addAttribute("statusStage", "Final");
        model.addAttribute("status", false);
        return "adminUpdateStatusWindow";
    }

    @PostMapping("/admin/updateStatus/statusFinal/{id}")
    public String storeStatusFinal(@PathVariable("id") Long id, HttpServletRequest request, Model model) {
        TeamDto teamDto = teamService.getTeam(id);
        String status = request.getParameter("status");
        String reason = "";
        LocalDate today = LocalDate.now();

        teamDto.setFinalStatus("["+today+"] " +status);
        if(status.equals("탈락")) {
            reason = request.getParameter("reason");
            teamDto.setFinalStatusReason(reason);
        }
        teamDto.setTeamName(teamDto.getTeamName());
        teamDto.setTeamYear(teamDto.getTeamYear());
        teamDto.setTeamSemester(teamDto.getTeamSemester());
        teamService.saveTeam(teamDto);

        model.addAttribute("teamInfo", teamDto);
        model.addAttribute("statusStage", "Final");
        model.addAttribute("status", "end");

        return "adminUpdateStatusWindow";
    }

    public List<StudentDto> getUserAndTeam(int type, List<UserDto> userList, List<TeamDto> teamList) {
        List<StudentDto> studentList = new ArrayList<>();

        switch(type) {
            case 0:
                for (UserDto userDto : userList) {
                    StudentDto studentDto = new StudentDto();
                    studentDto.setUserDto(userDto);
                    boolean flag = false;
                    for (TeamDto teamDto : teamList) {
                        if (userDto.getTeamIdNum()!= null && userDto.getTeamIdNum().equals(teamDto.getTeamId())) {
                            studentDto.setTeamDto(teamDto);
                            flag = true;
                            break;
                        }
                    }
                    if(!flag) {
                        studentDto.setTeamDto(null);
                    }
                    studentList.add(studentDto);
                }
                return studentList;
            case 1:
                for(TeamDto teamDto : teamList) {
                    for(UserDto userDto : userList) {
                        StudentDto studentDto = new StudentDto();
                        if(teamDto.getTeamId().equals(userDto.getTeamIdNum())) {
                            studentDto.setUserDto(userDto);
                            studentDto.setTeamDto(teamDto);
                            studentList.add(studentDto);
                        }
                    }
                }
                return studentList;
        }
    return null;
    }

    @GetMapping("/admin/manageStudent")
    public String getStudentList(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        if(customUserDetails.getUserStatus()) {
            List<UserDto> userList = userService.getStudentUser();
            List<TeamDto> teamList = teamService.getTeamList();
            List<StudentDto> studentList = getUserAndTeam(0, userList, teamList);
            model.addAttribute("studentList", studentList);
            return "adminManageStudent";
        }else{
            return "alertPage";
        }
    }

    @GetMapping({"/admin/manageStudent/studentSearch", "/admin/manageStudent/teamSearch"})
    public String redirectToManageStudent() {
        return "redirect:/admin/manageStudent";
    }

    @PostMapping("/admin/manageStudent/studentSearch")
    public String getStudentInfo(HttpServletRequest request, Model model) {
        String searchType = request.getParameter("searchType");
        String inputText = request.getParameter("searchText");
        List<UserDto> userList;
        List<TeamDto> teamList;
        List<StudentDto> studentList = new ArrayList<>();
        if(searchType.equals("studentId")) {
            userList = userService.getUserByStudentInfo(0, inputText);
            teamList = teamService.getTeamList();
            studentList = getUserAndTeam(0, userList, teamList);
        } else if(searchType.equals("studentName")) {
            userList = userService.getUserByStudentInfo(1, inputText);
            teamList = teamService.getTeamList();
            studentList = getUserAndTeam(0, userList, teamList);
        } else if(searchType.equals("teamName")) {
            userList = userService.getStudentUser();
            teamList = teamService.getTeamListByName(inputText);
            studentList = getUserAndTeam(1, userList, teamList);
        }
        model.addAttribute("studentList", studentList);
        return "adminManageStudent";
    }

    @PostMapping("/admin/manageStudent/teamSearch")
    public String getTeamInfo(HttpServletRequest request, Model model) {
        String yearToSearch = request.getParameter("teamYear");
        String semesterToSearch = request.getParameter("teamSemester");

        List<UserDto> userList = userService.getStudentUser();
        List<TeamDto> teamList;
        List<StudentDto> studentList = new ArrayList<>();

        if(yearToSearch.equals("")) {
            if(!semesterToSearch.equals("")) {
                model.addAttribute("ERROR", "올바르게 선택되지 않았습니다.");
            }
            teamList = teamService.getTeamList();
        } else {
            teamList = teamService.searchTeamByTime(yearToSearch, semesterToSearch);
            model.addAttribute("yearSearched", yearToSearch);
            model.addAttribute("semSearched", semesterToSearch);
        }
        studentList = getUserAndTeam(1, userList, teamList);

        model.addAttribute("studentList", studentList);
        return "adminManageStudent";
    }

    @GetMapping("/admin/applicationForm")
    public String manageApplicationForm(Model model){
        List<ApplicationFormDto> applicationFormList = applicationFormService.getApplicationFormList();
        model.addAttribute("applicationFormList", applicationFormList);
        return "adminManageAppliForm";
    }

    @GetMapping("/admin/applicationForm/{id}")
    public ResponseEntity<Resource> applicationFormFileDownload(@PathVariable("id") Long fileId) throws IOException {
        ApplicationFormDto applicationFormDto = applicationFormService.getApplicationForm(fileId);
        Path appliFilePath = Paths.get(applicationFormDto.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(appliFilePath));
        String appliFilename = applicationFormDto.getOrigFilename();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(appliFilename, "utf-8") + "\"")
                .body(resource);
    }
}

