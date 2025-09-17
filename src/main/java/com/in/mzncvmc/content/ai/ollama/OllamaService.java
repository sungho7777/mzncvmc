package com.in.mzncvmc.content.ai.ollama;

import com.in.mzncvmc.content.bbs.posts.BbsPosts;
import com.in.mzncvmc.content.bbs.posts.BbsPostsDto;
import com.in.mzncvmc.content.bbs.posts.BbsPostsService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class OllamaService {

    private final ChatClient chatClient;
    private final ChatModel chatModel; // 직접 ChatModel 사용도 가능
    private final BbsPostsService bbsPostsService;

    @Autowired
    public OllamaService(ChatClient.Builder chatClientBuilder, ChatModel chatModel, BbsPostsService bbsPostsService) {
        this.chatClient = chatClientBuilder.build();
        this.chatModel = chatModel;
        this.bbsPostsService = bbsPostsService;
    }

    /**
     * 사용자 질문에 대한 AI 답변 생성 (ChatClient 사용)
     * @param userPrompt 사용자 입력 프롬프트
     * @return AI 답변
     */
    public String getAIResponse(String userPrompt) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            String result = chatClient.prompt()
                    .user(userPrompt)
                    .call()
                    .content();
            BbsPostsDto dto = new BbsPostsDto();
            dto.setCategoryId(6L);
            dto.setTitle(userPrompt);
            dto.setBbsContent(result);

            dto.setStatus("ACTIVE");
            dto.setAuthorName(username);

            bbsPostsService.insert(dto);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "AI 서비스 처리 중 오류가 발생했습니다: " + e.getMessage();
        }
    }

    /**
     * 직접 ChatModel을 사용하는 방법 (선택적)
     */
    public String getAIResponseDirect(String userPrompt) {
        try {
            return chatModel.call(userPrompt);

        } catch (Exception e) {
            e.printStackTrace();
            return "AI 서비스 처리 중 오류가 발생했습니다: " + e.getMessage();
        }
    }

    /**
     * 스트리밍 방식 응답
     */
    public void getStreamingResponse(String userPrompt,
                                     java.util.function.Consumer<String> responseConsumer) {
        try {
            chatClient.prompt()
                    .user(userPrompt)
                    .stream()
                    .content()
                    .doOnNext(responseConsumer::accept)
                    .subscribe();

        } catch (Exception e) {
            responseConsumer.accept("스트리밍 처리 중 오류: " + e.getMessage());
        }
    }


}
