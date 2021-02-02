package com.ttree.ttree.controller;

import com.ttree.ttree.domain.entity.Board;
import com.ttree.ttree.dto.*;
import com.ttree.ttree.service.*;
import com.ttree.ttree.util.MD5Generator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
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

    private List<BoardDto> boardSearchList;

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

    @GetMapping("/projectList") //검색하지 않은 상태에서의 게시판
    public String list(Model model) {
        List<BoardDto> boardDtoList = boardService.getBoardList(null, null);
        //Integer[] pageList = boardService.getPageList(pageNum);
        //Page<Board> pageList = boardService.getBoardPage(pageable);

        //model.addAttribute("postList", boardDtoList);
        model.addAttribute("listPage", boardDtoList);
        //model.addAttribute("pageList", pageList);

        //System.out.println("총 element 수: " + pageList.getTotalElements());
        //System.out.println("전체 page 수: " + pageList.getTotalPages());
        //System.out.println("페이지에 표시할 element 수: " + pageList.getSize());
        //System.out.println("현재 페이지 index: " + pageList.getNumber());
        //System.out.println("현재 페이지의 element 수: " + pageList.getNumberOfElements());

        return "projectList";
    }

    @PostMapping("/projectList")
    public String search(HttpServletRequest request, Model model) {
        String[] yearToSearch = request.getParameterValues("year_select");
        String[] langToSearch = request.getParameterValues("language_select");
        List<BoardDto> boardDtoList = null;
        //Integer[] pageList = boardService.getPageList(pageNum);
        //List<BoardDto> boardDtoSearchList = boardService.getBoardList("year", yearToSearch[i]);

        if((!yearToSearch[0].equals("all_year")) && (yearToSearch != null)) {
            for(int i = 0; i < yearToSearch.length; i++) {
                List<BoardDto> boardDtoSearchList = boardService.getBoardList("year", yearToSearch[i]);
                if(boardDtoSearchList != null) {
                    for(int j = 0; j < boardDtoSearchList.size(); j++) {
                        if (boardDtoList == null) {
                            boardDtoList = boardDtoSearchList;
                            boardSearchList = boardDtoList;
                            break;
                        } else {
                            boardDtoList.add(boardDtoList.size(), boardDtoSearchList.get(j));
                        }
                    }
                }
            }
        } else {
            boardDtoList = boardService.getBoardList(null, null);
            boardSearchList = boardDtoList;
        }

        if((!langToSearch[0].equals("all_language")) && (langToSearch != null)) {
            boardDtoList = boardService.getBoardListFromLang(langToSearch, boardDtoList, languageService);
            boardSearchList = boardDtoList;
            //System.out.println("boardDtoSearchListLang pageNum:" + pageNum);
        }

        model.addAttribute("listPage", boardDtoList);
        //model.addAttribute("pageList", pageList);
        return "projectList";
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
            boardDto.setLanguages(langList);
            Long id = boardService.savePost(boardDto);

            // 소스코드
            if(!sourceFile.isEmpty()) {
                String origSourceFilename = sourceFile.getOriginalFilename();
                String sourceFilename = new MD5Generator(origSourceFilename).toString();

                String saveSourcePath = System.getProperty("user.dir") + "/sourceFiles";
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
                sourceFileDto.setSource_id(id);
                sourceFileDto.setSource_origFilename(origSourceFilename);
                sourceFileDto.setSource_filename(sourceFilename);
                sourceFileDto.setSource_filePath(sourceFilePath);

                sourceFileService.saveSourceFile(sourceFileDto);
            }

            // 논문
            if(!paperFile.isEmpty()) {
                String origPaperFilename = paperFile.getOriginalFilename();
                String paperFilename = new MD5Generator(origPaperFilename).toString();
                String savePaperPath = System.getProperty("user.dir") + "/paperFiles";
                if (!new File(savePaperPath).exists()) {
                    try {
                        new File(savePaperPath).mkdir();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
                String paperFilePath = savePaperPath + "/" + paperFilename;
                paperFile.transferTo(new File(paperFilePath));

                PaperFileDto paperFileDto = new PaperFileDto();
                paperFileDto.setPaper_id(id);
                paperFileDto.setPaper_origFilename(origPaperFilename);
                paperFileDto.setPaper_filename(paperFilename);
                paperFileDto.setPaper_filePath(paperFilePath);

                paperFileService.savePaperFile(paperFileDto);
            }


            // 제안서
            if(!proposalFile.isEmpty()) {
                String origProposalFilename = proposalFile.getOriginalFilename();
                String proposalFilename = new MD5Generator(origProposalFilename).toString();
                String saveProposalPath = System.getProperty("user.dir") + "/proposalFiles";
                if (!new File(saveProposalPath).exists()) {
                    try {
                        new File(saveProposalPath).mkdir();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
                String proposalFilePath = saveProposalPath + "/" + proposalFilename;
                proposalFile.transferTo(new File(proposalFilePath));

                ProposalFileDto proposalFileDto = new ProposalFileDto();
                proposalFileDto.setProposal_id(id);
                proposalFileDto.setProposal_origFilename(origProposalFilename);
                proposalFileDto.setProposal_filename(proposalFilename);
                proposalFileDto.setProposal_filePath(proposalFilePath);

                proposalFileService.saveProposalFile(proposalFileDto);
            }


            // 전시회자료
            if(!fairFile.isEmpty()) {
                String origFairFilename = fairFile.getOriginalFilename();
                String fairFilename = new MD5Generator(origFairFilename).toString();
                String saveFairPath = System.getProperty("user.dir") + "/fairFiles";
                if (!new File(saveFairPath).exists()) {
                    try {
                        new File(saveFairPath).mkdir();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
                String fairFilePath = saveFairPath + "/" + fairFilename;
                fairFile.transferTo(new File(fairFilePath));

                FairFileDto fairFileDto = new FairFileDto();
                fairFileDto.setFair_id(id);
                fairFileDto.setFair_origFilename(origFairFilename);
                fairFileDto.setFair_filename(fairFilename);
                fairFileDto.setFair_filePath(fairFilePath);

                fairFileService.saveFairFile(fairFileDto);
            }


            // 최종발표
            if(!finalPTFile.isEmpty()) {
                String origFinalPTFilename = finalPTFile.getOriginalFilename();
                String finalPTFilename = new MD5Generator(origFinalPTFilename).toString();
                String saveFinalPTPath = System.getProperty("user.dir") + "/finalPTFiles";
                if (!new File(saveFinalPTPath).exists()) {
                    try {
                        new File(saveFinalPTPath).mkdir();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
                String finalPTFilePath = saveFinalPTPath + "/" + finalPTFilename;
                finalPTFile.transferTo(new File(finalPTFilePath));

                FinalPTFileDto finalPTFileDto = new FinalPTFileDto();
                finalPTFileDto.setFinalPT_id(id);
                finalPTFileDto.setFinalPT_origFilename(origFinalPTFilename);
                finalPTFileDto.setFinalPT_filename(finalPTFilename);
                finalPTFileDto.setFinalPT_filePath(finalPTFilePath);

                finalPTFileService.saveFinalPTFile(finalPTFileDto);
            }

            LanguageDto languageDto = new LanguageDto();
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
                languageDto.setBoard_id(id);
                languageService.saveLanguage(languageDto);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "redirect:/projectList";
    }

    @GetMapping("/projectPost/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        String langList = languageService.getLangList(id);
        BoardDto boardDto = boardService.getPost(id);

        int hit = boardDto.getHit();
        hit++;
        boardDto.setHit(hit);
        boardService.savePost(boardDto);

        String proposalFileName = "";
        String finalPTFileName = "";
        String fairFileName = "";
        String sourceFileName = "";
        String paperFileName = "";
        if (proposalFileService.getProposalFile(id) != null) {
            proposalFileName = proposalFileService.getProposalFile(id).getProposal_origFilename();
        }
        if (finalPTFileService.getFinalPTFile(id) != null) {
            finalPTFileName = finalPTFileService.getFinalPTFile(id).getFinalPT_origFilename();
        }
        if (fairFileService.getFairFile(id) != null) {
            fairFileName = fairFileService.getFairFile(id).getFair_origFilename();
        }
        if (sourceFileService.getSourceFile(id) != null) {
            sourceFileName = sourceFileService.getSourceFile(id).getSource_origFilename();
        }
        if (paperFileService.getPaperFile(id) != null) {
            paperFileName = paperFileService.getPaperFile(id).getPaper_origFilename();
        }

        model.addAttribute("post", boardDto);
        model.addAttribute("postLang", langList);
        model.addAttribute("proposalFileName", proposalFileName);
        model.addAttribute("finalPTFileName", finalPTFileName);
        model.addAttribute("fairFileName", fairFileName);
        model.addAttribute("sourceFileName", sourceFileName);
        model.addAttribute("paperFileName", paperFileName);

        return "projectDetail";
    }

    @GetMapping("/projectPost/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);
        String langList = languageService.getLangList(id);
        String proposalFileName = "";
        String finalPTFileName = "";
        String fairFileName = "";
        String sourceFileName = "";
        String paperFileName = "";
        if(proposalFileService.getProposalFile(id) != null) {
            proposalFileName = proposalFileService.getProposalFile(id).getProposal_origFilename();
        }
        if(finalPTFileService.getFinalPTFile(id) != null) {
            finalPTFileName = finalPTFileService.getFinalPTFile(id).getFinalPT_origFilename();
        }
        if(fairFileService.getFairFile(id) != null) {
            fairFileName = fairFileService.getFairFile(id).getFair_origFilename();
        }
        if(sourceFileService.getSourceFile(id) != null) {
            sourceFileName = sourceFileService.getSourceFile(id).getSource_origFilename();
        }
        if(paperFileService.getPaperFile(id) != null) {
            paperFileName = paperFileService.getPaperFile(id).getPaper_origFilename();
        }
        model.addAttribute("post", boardDto);
        model.addAttribute("postLang", langList);
        model.addAttribute("proposalFileName", proposalFileName);
        model.addAttribute("finalPTFileName", finalPTFileName);
        model.addAttribute("fairFileName", fairFileName);
        model.addAttribute("sourceFileName", sourceFileName);
        model.addAttribute("paperFileName", paperFileName);
        return "projectEdit";
    }

    @PostMapping("/projectPost/edit/{id}")
    public String update(@RequestParam("id") Long id, @RequestParam("sourceFile") MultipartFile sourceFile,
                         @RequestParam("paperFile") MultipartFile paperFile, @RequestParam("proposalFile") MultipartFile proposalFile,
                         @RequestParam("finalPTFile") MultipartFile finalPTFile, @RequestParam("fairFile") MultipartFile fairFile,
                         BoardDto boardDto, @RequestParam("checkbox") List<String> langList, HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        boardDto.setId(id);
        boardService.savePost(boardDto);

        // 소스코드
        if(!sourceFile.isEmpty()) {
            String origSourceFilename = sourceFile.getOriginalFilename();
            String sourceFilename = new MD5Generator(origSourceFilename).toString();

            String saveSourcePath = System.getProperty("user.dir") + "/sourceFiles";
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
            sourceFileDto.setSource_id(id);
            sourceFileDto.setSource_origFilename(origSourceFilename);
            sourceFileDto.setSource_filename(sourceFilename);
            sourceFileDto.setSource_filePath(sourceFilePath);

            sourceFileService.saveSourceFile(sourceFileDto);
        }

        // 논문
        if(!paperFile.isEmpty()) {
            String origPaperFilename = paperFile.getOriginalFilename();
            String paperFilename = new MD5Generator(origPaperFilename).toString();
            String savePaperPath = System.getProperty("user.dir") + "/paperFiles";
            if (!new File(savePaperPath).exists()) {
                try {
                    new File(savePaperPath).mkdir();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
            String paperFilePath = savePaperPath + "/" + paperFilename;
            paperFile.transferTo(new File(paperFilePath));

            PaperFileDto paperFileDto = new PaperFileDto();
            paperFileDto.setPaper_id(id);
            paperFileDto.setPaper_origFilename(origPaperFilename);
            paperFileDto.setPaper_filename(paperFilename);
            paperFileDto.setPaper_filePath(paperFilePath);

            paperFileService.savePaperFile(paperFileDto);
        }


        // 제안서
        if(!proposalFile.isEmpty()) {
            String origProposalFilename = proposalFile.getOriginalFilename();
            String proposalFilename = new MD5Generator(origProposalFilename).toString();
            String saveProposalPath = System.getProperty("user.dir") + "/proposalFiles";
            if (!new File(saveProposalPath).exists()) {
                try {
                    new File(saveProposalPath).mkdir();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
            String proposalFilePath = saveProposalPath + "/" + proposalFilename;
            proposalFile.transferTo(new File(proposalFilePath));

            ProposalFileDto proposalFileDto = new ProposalFileDto();
            proposalFileDto.setProposal_id(id);
            proposalFileDto.setProposal_origFilename(origProposalFilename);
            proposalFileDto.setProposal_filename(proposalFilename);
            proposalFileDto.setProposal_filePath(proposalFilePath);

            proposalFileService.saveProposalFile(proposalFileDto);
        }


        // 전시회자료
        if(!fairFile.isEmpty()) {
            String origFairFilename = fairFile.getOriginalFilename();
            String fairFilename = new MD5Generator(origFairFilename).toString();
            String saveFairPath = System.getProperty("user.dir") + "/fairFiles";
            if (!new File(saveFairPath).exists()) {
                try {
                    new File(saveFairPath).mkdir();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
            String fairFilePath = saveFairPath + "/" + fairFilename;
            fairFile.transferTo(new File(fairFilePath));

            FairFileDto fairFileDto = new FairFileDto();
            fairFileDto.setFair_id(id);
            fairFileDto.setFair_origFilename(origFairFilename);
            fairFileDto.setFair_filename(fairFilename);
            fairFileDto.setFair_filePath(fairFilePath);

            fairFileService.saveFairFile(fairFileDto);
        }


        // 최종발표
        if(!finalPTFile.isEmpty()) {
            String origFinalPTFilename = finalPTFile.getOriginalFilename();
            String finalPTFilename = new MD5Generator(origFinalPTFilename).toString();
            String saveFinalPTPath = System.getProperty("user.dir") + "/finalPTFiles";
            if (!new File(saveFinalPTPath).exists()) {
                try {
                    new File(saveFinalPTPath).mkdir();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
            String finalPTFilePath = saveFinalPTPath + "/" + finalPTFilename;
            finalPTFile.transferTo(new File(finalPTFilePath));

            FinalPTFileDto finalPTFileDto = new FinalPTFileDto();
            finalPTFileDto.setFinalPT_id(id);
            finalPTFileDto.setFinalPT_origFilename(origFinalPTFilename);
            finalPTFileDto.setFinalPT_filename(finalPTFilename);
            finalPTFileDto.setFinalPT_filePath(finalPTFilePath);

            finalPTFileService.saveFinalPTFile(finalPTFileDto);
        }

        LanguageDto languageDto = new LanguageDto();
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
            languageDto.setBoard_id(id);
            languageService.saveLanguage(languageDto);
        }

        return "redirect:/projectList";
    }

    @DeleteMapping("/projectPost/{id}")
    public String delete(@PathVariable("id") Long id) {
        boardService.deletePost(id);
        languageService.deleteLanguage(id);
        proposalFileService.deleteProposalFile(id);
        finalPTFileService.deleteFinalPTFile(id);
        fairFileService.deleteFairFile(id);
        sourceFileService.deleteSourceFile(id);
        paperFileService.deletePaperFile(id);

        return "redirect:/projectList";
    }

    @GetMapping("/download/source/{id}")
    public ResponseEntity<Resource> sourceFileDownload(@PathVariable("id") Long fileId) throws IOException {
        SourceFileDto sourceFileDto = sourceFileService.getSourceFile(fileId);
        Path sourcePath = Paths.get(sourceFileDto.getSource_filePath());
        Resource resource = new InputStreamResource(Files.newInputStream(sourcePath));
        String sourceFilename = sourceFileDto.getSource_origFilename();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(sourceFilename, "utf-8") + "\"")
                .body(resource);
    }

    @GetMapping("/download/paper/{id}")
    public ResponseEntity<Resource> paperFileDownload(@PathVariable("id") Long fileId) throws IOException {
        PaperFileDto paperFileDto = paperFileService.getPaperFile(fileId);
        Path paperPath = Paths.get(paperFileDto.getPaper_filePath());
        Resource resource = new InputStreamResource(Files.newInputStream(paperPath));
        String paperFilename = paperFileDto.getPaper_origFilename();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(paperFilename, "utf-8") + "\"")
                .body(resource);
    }

    @GetMapping("/download/proposal/{id}")
    public ResponseEntity<Resource> proposalFileDownload(@PathVariable("id") Long fileId) throws IOException {
        ProposalFileDto proposalFileDto = proposalFileService.getProposalFile(fileId);
        Path proposalPath = Paths.get(proposalFileDto.getProposal_filePath());
        Resource resource = new InputStreamResource(Files.newInputStream(proposalPath));
        String proposalFilename = proposalFileDto.getProposal_origFilename();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(proposalFilename, "utf-8") + "\"")
                .body(resource);
    }


    @GetMapping("/download/finalPT/{id}")
    public ResponseEntity<Resource> finalPTFileDownload(@PathVariable("id") Long fileId) throws IOException {
        FinalPTFileDto finalPTFileDto = finalPTFileService.getFinalPTFile(fileId);
        Path finalPTPath = Paths.get(finalPTFileDto.getFinalPT_filePath());
        Resource resource = new InputStreamResource(Files.newInputStream(finalPTPath));
        String finalPTFilename = finalPTFileDto.getFinalPT_origFilename();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(finalPTFilename, "utf-8") + "\"")
                .body(resource);
    }

    @GetMapping("/download/fair/{id}")
    public ResponseEntity<Resource> fairFileDownload(@PathVariable("id") Long fileId) throws IOException {
        FairFileDto fairFileDto = fairFileService.getFairFile(fileId);
        Path fairPath = Paths.get(fairFileDto.getFair_filePath());
        Resource resource = new InputStreamResource(Files.newInputStream(fairPath));
        String fairFilename = fairFileDto.getFair_origFilename();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(fairFilename, "utf-8") + "\"")
                .body(resource);
    }

}