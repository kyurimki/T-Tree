package ttree.it.ttreeGradle.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ttree.it.ttreeGradle.dto.MailDto;
import ttree.it.ttreeGradle.dto.SourceFileDto;
import ttree.it.ttreeGradle.service.MailService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
public class MainController {

    private final MailService mailService;

    public MainController(MailService mailService) {
        this.mailService = mailService;
    }

    @RequestMapping(value = "/")
    public String home(){
        return "index";
    }

    @RequestMapping(value = "/accessDeniedPage")
    public String accessDeniedPage() { return "accessDeniedPage"; }

    @RequestMapping(value = "/alertPage")
    public String alertPage(){ return "alertPage"; }

    @RequestMapping(value = "/user/login")
    public String login(){ return "login"; }

    @GetMapping("/projectProcess/download")
    public ResponseEntity<Resource> projectProposalDownload(HttpServletRequest request) throws IOException {
        if(request.getHeader("REFERER") == null){
            return ResponseEntity.ok().body(null);
        }else {
            String filename = "IT공학과_졸업작품신청서.hwp";
            String path = System.getProperty("user.dir") + "/registerFile/"+filename;
            Path sourcePath = Paths.get(path);
            Resource resource = new InputStreamResource(Files.newInputStream(sourcePath));

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(filename, "utf-8") + "\"")
                    .body(resource);
        }
    }

    @PostMapping("/mail")
    public void execMail(MailDto mailDto) {
        mailService.mailSend(mailDto);
    }

}