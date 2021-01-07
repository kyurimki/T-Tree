package com.ttree.ttree.controller;

import com.ttree.ttree.dto.BoardDto;
import com.ttree.ttree.dto.LanguageDto;
import com.ttree.ttree.dto.PaperFileDto;
import com.ttree.ttree.dto.SourceFileDto;
import com.ttree.ttree.service.BoardService;
import com.ttree.ttree.service.LanguageService;
import com.ttree.ttree.service.PaperFileService;
import com.ttree.ttree.service.SourceFileService;
import com.ttree.ttree.util.MD5Generator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class BoardController {
    private BoardService boardService;
    private SourceFileService sourceFileService;
    private PaperFileService paperFileService;
    private LanguageService languageService;
    private Model model;
    private List<String> yearToSearch;
    private List<String> langToSearch;

    public BoardController(BoardService boardService, SourceFileService sourceFileService, PaperFileService paperFileService, LanguageService languageService) {
        this.boardService = boardService;
        this.sourceFileService = sourceFileService;
        this.paperFileService = paperFileService;
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
    public String write(@RequestParam("sourceFile") MultipartFile sourceFile, @RequestParam("paperFile") MultipartFile paperFile, BoardDto boardDto, @RequestParam("checkbox") List<String> langList) {
        try {
            String origSourceFilename = sourceFile.getOriginalFilename();
            String sourceFilename = new MD5Generator(origSourceFilename).toString();
            /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
            String saveSourcePath = System.getProperty("user.dir") + "\\sourceFiles";
            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
            if (!new File(saveSourcePath).exists()) {
                try{
                    new File(saveSourcePath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }
            String sourceFilePath = saveSourcePath + "\\" + sourceFilename;
            sourceFile.transferTo(new File(sourceFilePath));

            SourceFileDto sourceFileDto = new SourceFileDto();
            sourceFileDto.setSource_origFilename(origSourceFilename);
            sourceFileDto.setSource_filename(sourceFilename);
            sourceFileDto.setSource_filePath(sourceFilePath);

            Long sourceFileId = sourceFileService.saveSourceFile(sourceFileDto);

            String origPaperFilename = paperFile.getOriginalFilename();
            String paperFilename = new MD5Generator(origPaperFilename).toString();
            /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
            String savePaperPath = System.getProperty("user.dir") + "\\paperFiles";
            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
            if (!new File(savePaperPath).exists()) {
                try{
                    new File(savePaperPath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }
            String paperFilePath = savePaperPath + "\\" + paperFilename;
            paperFile.transferTo(new File(paperFilePath));

            PaperFileDto paperFileDto = new PaperFileDto();
            paperFileDto.setPaper_id(sourceFileId);
            paperFileDto.setPaper_origFilename(origPaperFilename);
            paperFileDto.setPaper_filename(paperFilename);
            paperFileDto.setPaper_filePath(paperFilePath);

            paperFileService.savePaperFile(paperFileDto);

            LanguageDto languageDto = new LanguageDto();
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
                    languageDto.setLang_etc(true);
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
}