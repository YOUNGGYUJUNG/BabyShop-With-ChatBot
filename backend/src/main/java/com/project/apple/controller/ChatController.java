package com.project.apple.controller;

import com.project.apple.dto.chat.ChatMessage;
import com.project.apple.service.AIChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin(originPatterns = "*")
@RequiredArgsConstructor
public class ChatController {

    private final AIChatService aiChatService;

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        // 사용자 메시지 처리
        if (!chatMessage.getSender().equals("AI Assistant")) {
            // 비동기로 AI 응답 처리
            CompletableFuture.runAsync(() -> {
                aiChatService.processAIResponse(chatMessage);
            });
        }
        return chatMessage;
    }
}