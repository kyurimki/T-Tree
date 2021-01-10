package com.ttree.ttree.controller;

import com.ttree.ttree.domain.entity.Language;
import com.ttree.ttree.dto.*;
import com.ttree.ttree.service.*;
import com.ttree.ttree.util.MD5Generator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class BoardController {
    private BoardService boardService;
    private LanguageService languageService;

    private SourceFileService sourceFileService;
    private PaperFileService paperFileService;
    private ProposalFileService proposalFileService;
    private FinalPTFileService finalPTFileService;
    private FairFileService fairFileService;


    private Model model;
    private List<String> yearToSearch;
    private List<String> langToSearch;

    public BoardController(BoardService boardService, SourceFileService sourceFileService, PaperFileService paperFileService, LanguageService languageService,
                            ProposalFileService proposalFileService, FinalPTFileService finalPTFileService, FairFileService fairFileService ) {
        this.boardService = boardService;
        this.sourceFileService = sourceFileService;
        this.paperFileService = paperFileService;
        this.proposalFileService = proposalFileService;
        this.finalPTFileService = finalPTFileService;
        this.fairFileService = fairFileService;
        this.languageService = languageService;
    }

    @GetMapping("/projectList")
    public String list(Model model) {
        List<BoardDto> boardDtoList = null;
        if(yearToSearch == null) {
            boardDtoList = boardService.getBoardList();
            model.addAttribute("postList", boardDtoList);
            return "projectList";
        } else {
            for(String year : yearToSearch) {
                boardDtoList = boardService.getBoardList("year", year);
            }
            model.addAttribute("postList", boardDtoList);
            return "projectList";
        }
    }

    @PostMapping("/projectList")
    public void search(/*@RequestParam("year_select") List<String> yearToSearch, @RequestParam("language_select") List<String> langToSearch*/) {
//        if(yearToSearch != null) {
//            this.yearToSearch = yearToSearch;
//            System.out.println("#");
//        }
//        if(langToSearch != null) {
//            this.langToSearch = langToSearch;
//        }
    }

    @GetMapping("/projectPost")
    public String post() {
        return "projectPost";
    }


    @PostMapping("/projectPost")
    public String write(@RequestParam("sourceFile") MultipartFile sourceFile, @RequestParam("paperFile") MultipartFile paperFile,
                        @RequestParam("proposalFile") MultipartFile proposalFile, @RequestParam("finalPTFile") MultipartFile finalPTFile,
                        @RequestParam("fairFile") MultipartFile fairFile,
                        BoardDto boardDto, @RequestParam("checkbox") List<String> langList, HttpServletRequest request) {
        try {
            //소스코드
            String origSourceFilename = sourceFile.getOriginalFilename();
            String sourceFilename = new MD5Generator(origSourceFilename).toString();
            /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
            String saveSourcePath = System.getProperty("user.dir") + "/sourceFiles";
            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
            if (!new File(saveSourcePath).exists()) {
                try{
                    new File(saveSourcePath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }
            String sourceFilePath = saveSourcePath + "/" + sourceFilename;
            sourceFile.transferTo(new File(sourceFilePath));

            SourceFileDto sourceFileDto = new SourceFileDto();
            sourceFileDto.setSource_origFilename(origSourceFilename);
            sourceFileDto.setSource_filename(sourceFilename);
            sourceFileDto.setSource_filePath(sourceFilePath);
            Long sourceFileId = sourceFileService.saveSourceFile(sourceFileDto);


            //논문 자료
            String origPaperFilename = paperFile.getOriginalFilename();
            String paperFilename = new MD5Generator(origPaperFilename).toString();
            /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
            String savePaperPath = System.getProperty("user.dir") + "/paperFiles";
            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
            if (!new File(savePaperPath).exists()) {
                try{
                    new File(savePaperPath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }
            String paperFilePath = savePaperPath + "/" + paperFilename;
            paperFile.transferTo(new File(paperFilePath));

            PaperFileDto paperFileDto = new PaperFileDto();
            paperFileDto.setPaper_id(sourceFileId);
            paperFileDto.setPaper_origFilename(origPaperFilename);
            paperFileDto.setPaper_filename(paperFilename);
            paperFileDto.setPaper_filePath(paperFilePath);
            paperFileService.savePaperFile(paperFileDto);


            //제안서
            String origProposalFilename = proposalFile.getOriginalFilename();
            String proposalFilename = new MD5Generator(origProposalFilename).toString();
            /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
            String saveProposalPath = System.getProperty("user.dir") + "\\proposalFiles";
            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
            if (!new File(saveProposalPath).exists()) {
                try{
                    new File(saveProposalPath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }
            String proposalFilePath = saveProposalPath + "\\" + proposalFilename;
            proposalFile.transferTo(new File(proposalFilePath));

            ProposalFileDto proposalFileDto = new ProposalFileDto();
            proposalFileDto.setProposal_id(sourceFileId);
            proposalFileDto.setProposal_origFilename(origProposalFilename);
            proposalFileDto.setProposal_filename(proposalFilename);
            proposalFileDto.setProposal_filePath(proposalFilePath);
            proposalFileService.saveProposalFile(proposalFileDto);


            //전시회자료
            String origFairFilename = fairFile.getOriginalFilename();
            String fairFilename = new MD5Generator(origFairFilename).toString();
            /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
            String saveFairPath = System.getProperty("user.dir") + "\\fairFiles";
            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
            if (!new File(saveFairPath).exists()) {
                try{
                    new File(saveFairPath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }
            String fairFilePath = saveFairPath + "\\" + fairFilename;
            fairFile.transferTo(new File(fairFilePath));
            
            FairFileDto fairFileDto = new FairFileDto();
            fairFileDto.setFair_id(sourceFileId);
            fairFileDto.setFair_origFilename(origFairFilename);
            fairFileDto.setFair_filename(fairFilename);
            fairFileDto.setFair_filePath(fairFilePath);
            fairFileService.saveFairFile(fairFileDto);



            //최종발표
            String origFinalPTFilename = finalPTFile.getOriginalFilename();
            String finalPTFilename = new MD5Generator(origFinalPTFilename).toString();
            /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
            String saveFinalPTPath = System.getProperty("user.dir") + "\\finalPTFiles";
            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
            if (!new File(saveFinalPTPath).exists()) {
                try{
                    new File(saveFinalPTPath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }
            String finalPTFilePath = saveFinalPTPath + "\\" + finalPTFilename;
            finalPTFile.transferTo(new File(finalPTFilePath));

            FinalPTFileDto finalPTFileDto = new FinalPTFileDto();
            finalPTFileDto.setFinalPT_id(sourceFileId);
            finalPTFileDto.setFinalPT_origFilename(origFinalPTFilename);
            finalPTFileDto.setFinalPT_filename(fairFilename);
            finalPTFileDto.setFinalPT_filePath(finalPTFilePath);
            finalPTFileService.saveFinalPTFile(finalPTFileDto);


            LanguageDto languageDto = new LanguageDto();

            //String etcDetail = "sad";

            String etcDetail = request.getParameter("etcText");
            for(String lang : langList) {
                if(lang.equals("android")) {
                    languageDto.setLang_android(true);
                } else if(lang.equals("cpp")) {
                    languageDto.setLang_cpp(true);
                } else if(lang.equals("django")) {
                    languageDto.setLang_django(true);
                } else if(lang.equals("html")) {
                    languageDto.setLang_html(true);
                } else if(lang.equals("java")) {
                    languageDto.setLang_java(true);
                } else if(lang.equals("nodejs")) {
                    languageDto.setLang_nodejs(true);
                } else if(lang.equals("python")) {
                    languageDto.setLang_python(true);
                } else if(lang.equals("rn")) {
                    languageDto.setLang_react(true);
                } else if(lang.equals("spring")) {
                    languageDto.setLang_spring(true);
                } else if(lang.equals("vuejs")) {
                    languageDto.setLang_vuejs(true);
                } else if(lang.equals("etc")) {
                    languageDto.setLang_etc(etcDetail);
                }
                languageDto.setBoard_id(sourceFileId);
                languageService.saveLanguage(languageDto);
            }

            boardDto.setId(sourceFileId);
            boardService.savePost(boardDto);


        } catch(Exception e) {
            e.printStackTrace();
        }
        return "redirect:/projectList";
    }

    @GetMapping("/projectPost/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);
        model.addAttribute("post", boardDto);
        return "projectDetail";
    }

    @GetMapping("/projectPost/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);
        model.addAttribute("post", boardDto);
        return "projectEdit";
    }

    @PostMapping("/projectPost/edit/{id}")
    public String update(BoardDto boardDto) {
        boardService.savePost(boardDto);
        return "redirect:/projectList";
    }

    @DeleteMapping("/projectPost/{id}")
    public String delete(@PathVariable("id") Long id) {
        boardService.deletePost(id);
        sourceFileService.deleteSourceFile(id);
        paperFileService.deletePaperFile(id);
        return "redirect:/projectList";
    }

    @GetMapping("/download/source/{id}")
    public ResponseEntity<Resource> sourceFileDownload(@PathVariable("id") Long fileId) throws IOException {
        SourceFileDto sourceFileDto = sourceFileService.getSourceFile(fileId);
        Path sourcePath = Paths.get(sourceFileDto.getSource_filePath());
        Resource resource = new InputStreamResource(Files.newInputStream(sourcePath));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + sourceFileDto.getSource_origFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/download/paper/{id}")
    public ResponseEntity<Resource> paperFileDownload(@PathVariable("id") Long fileId) throws IOException {
        PaperFileDto paperFileDto = paperFileService.getPaperFile(fileId);
        Path paperPath = Paths.get(paperFileDto.getPaper_filePath());
        Resource resource = new InputStreamResource(Files.newInputStream(paperPath));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + paperFileDto.getPaper_origFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/download/proposal/{id}")
    public ResponseEntity<Resource> proposalFileDownload(@PathVariable("id") Long fileId) throws IOException {
        ProposalFileDto proposalFileDto = proposalFileService.getProposalFile(fileId);
        Path proposalPath = Paths.get(proposalFileDto.getProposal_filePath());
        Resource resource = new InputStreamResource(Files.newInputStream(proposalPath));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + proposalFileDto.getProposal_origFilename() + "\"")
                .body(resource);
    }


    @GetMapping("/download/finalPT/{id}")
    public ResponseEntity<Resource> finalPTFileDownload(@PathVariable("id") Long fileId) throws IOException {
        FinalPTFileDto finalPTFileDto = finalPTFileService.getFinalPTFile(fileId);
        Path finalPTPath = Paths.get(finalPTFileDto.getFinalPT_filePath());
        Resource resource = new InputStreamResource(Files.newInputStream(finalPTPath));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + finalPTFileDto.getFinalPT_origFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/download/fair/{id}")
    public ResponseEntity<Resource> fairFileDownload(@PathVariable("id") Long fileId) throws IOException {
        FairFileDto fairFileDto = fairFileService.getFairFile(fileId);
        Path fairPath = Paths.get(fairFileDto.getFair_filePath());
        Resource resource = new InputStreamResource(Files.newInputStream(fairPath));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fairFileDto.getFair_origFilename() + "\"")
                .body(resource);
    }

}