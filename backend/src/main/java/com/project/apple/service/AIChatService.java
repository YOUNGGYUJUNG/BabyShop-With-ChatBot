package com.project.apple.service;

import com.project.apple.dto.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class AIChatService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final WebClient webClient;

    // \n을 줄바꿈 처리하는 메서드
    private String formatResponse(String response) {
        // \n을 실제 줄바꿈으로 변환하고 큰따옴표 제거
        return response.replace("\"", "")
                .replace("\\n", "<br>")
                .replace("\\https", "\"https")
                .replace("px;\\>", "px;\">")
                .replace("\\ style=", "\" style=\"");

    }

    public AIChatService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:5000").build();
    }

    public void processAIResponse(ChatMessage userMessage) {
        // AI 응답 생성
        ChatMessage aiResponse = createAIResponse(userMessage);

        // 응답 포맷 변경: \n을 줄바꿈 처리
        String formattedMessage = formatResponse(aiResponse.getResponse());
        aiResponse.setResponse(formattedMessage);

        // 약간의 지연을 주어 실제 AI가 응답하는 것처럼 보이게 함
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // WebSocket을 통해 응답 전송
        messagingTemplate.convertAndSend("/topic/public", aiResponse);
    }

    private ChatMessage createAIResponse(ChatMessage userMessage) {

        ChatMessage aiMessage = new ChatMessage();


        String response = webClient.post()
                .uri("/process")
                .bodyValue(Map.of("message", userMessage.getResponse()))
                .retrieve()
                .bodyToMono(String.class)
                .block();// Blocking for simple

        // 간단한 응답 로직 구현
        aiMessage.setSender("AI Assistant");
        aiMessage.setType(ChatMessage.MessageType.CHAT);
        aiMessage.setResponse(response);

        return aiMessage;
    }


}
