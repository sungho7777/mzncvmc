package com.in.mzncvmc.content.ai.ollama;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.in.mzncvmc.content.common.constants.CommonConstants.SLASH_API;

@Log4j2
@RestController
@RequestMapping(SLASH_API + "/ai/ollama")
public class OllamaRestController {
    private final OllamaService ollamaService;

    @Autowired
    public OllamaRestController(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }


    /**
     * AI 질문 처리 (AJAX)
     */
    @PostMapping("/ask")
    //@PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> askAI(@RequestBody Map<String, String> request) {

        Map<String, Object> response = new HashMap<>();

        try {
            String userPrompt = request.get("prompt");

            // 입력 검증
            if (userPrompt == null || userPrompt.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "질문을 입력해주세요.");
                return ResponseEntity.badRequest().body(response);
            }

            // 프롬프트 길이 제한 (선택사항)
            if (userPrompt.length() > 2000) {
                response.put("success", false);
                response.put("message", "질문이 너무 깁니다. 2000자 이내로 입력해주세요.");
                return ResponseEntity.badRequest().body(response);
            }

            // AI 서비스 호출
            String aiAnswer = ollamaService.getAIResponse(userPrompt);

            response.put("success", true);
            response.put("answer", aiAnswer);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "AI 서비스 처리 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * AI 서비스 상태 확인
     */
    @GetMapping("/status")
    //@PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkStatus() {
        Map<String, Object> status = new HashMap<>();

        try {
            // 간단한 테스트 질문으로 서비스 상태 확인
            String testResponse = ollamaService.getAIResponse("Hello");

            status.put("status", "online");
            status.put("message", "AI 서비스가 정상 작동 중입니다.");

        } catch (Exception e) {
            status.put("status", "offline");
            status.put("message", "AI 서비스에 연결할 수 없습니다.");
        }

        return ResponseEntity.ok(status);
    }


}
