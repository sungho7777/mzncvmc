package com.in.mzncvmc.content.bbs;

import com.in.mzncvmc.common.files.FileEntity;
import com.in.mzncvmc.common.files.FileUploadService;
import com.in.mzncvmc.content.users.UsersDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.in.mzncvmc.content.common.CommonConstants.*;
import static com.in.mzncvmc.content.common.CommonConstants.CONTENT_PAGE;

@Log4j2
@Controller
@RequestMapping("/m/bbs")
public class BbsController {
    private final String BBS = "bbs";
    private final String MENU_LIST_JSP = "bbs/list.jsp";

    private final FileUploadService fileUploadService;

    @Autowired
    public BbsController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    /**
     * 게시글 작성 페이지
     */
    @GetMapping(SLASH_LIST)
    public String listPage(Model model) {

        model.addAttribute(SIDEBAR, BBS);
        model.addAttribute(CONTENT_PAGE, MENU_LIST_JSP);
        return MAIN;
    }


    /**
     * 게시글 저장 (파일 업로드 포함)
     */
    @PostMapping("/save")
    public String saveBbs(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "files", required = false) MultipartFile[] files,
            Model model) {

        try {
            // 1. 게시글 저장 (실제 BBS 엔티티 저장 로직)
            Long bbsId = saveBbsPost(title, content); // 실제 구현 필요

            // 2. 첨부파일 업로드 (파일이 있는 경우)
            if (files != null && files.length > 0) {
                Long userId = getCurrentUserId(); // 현재 사용자 ID 가져오기
                List<FileEntity> uploadedFiles = fileUploadService.uploadFiles(files, "bbs", bbsId, userId);

                model.addAttribute("uploadedFiles", uploadedFiles);
            }

            return "redirect:/bbs/view/" + bbsId;

        } catch (Exception e) {
            model.addAttribute("error", "게시글 저장 중 오류가 발생했습니다: " + e.getMessage());
            return "list";
        }
    }

    /**
     * 게시글 상세보기 (첨부파일 목록 포함)
     */
    @GetMapping("/view/{bbsId}")
    public String viewBbs(@PathVariable Long bbsId, Model model) {
        // 1. 게시글 정보 조회
        // BbsEntity bbs = bbsService.findById(bbsId);
        // model.addAttribute("bbs", bbs);

        // 2. 첨부파일 목록 조회
        List<FileEntity> files = fileUploadService.getFilesByReference("bbs", bbsId);
        model.addAttribute("files", files);

        return "bbs/view";
    }

    private Long saveBbsPost(String title, String content) {
        // 실제 BBS 엔티티 저장 로직 구현
        // 예시로 임시 ID 반환
        return 1L;
    }

    private Long getCurrentUserId() {
        // 실제 현재 사용자 ID 조회 로직 구현
        // JWT에서 사용자 정보 추출
        return 1L;
    }


}
