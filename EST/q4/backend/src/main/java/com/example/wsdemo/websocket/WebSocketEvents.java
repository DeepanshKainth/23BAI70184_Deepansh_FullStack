package com.example.wsdemo.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.time.Instant;

@Component
public class WebSocketEvents {

    private static final Logger log = LoggerFactory.getLogger(WebSocketEvents.class);
    private final SimpMessageSendingOperations messagingTemplate;

    public WebSocketEvents(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void onSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination();

        if ("/topic/messages".equals(destination)) {
            log.info("Client subscribed to {}. Sending test message.", destination);
            messagingTemplate.convertAndSend(
                    "/topic/messages",
                new MessagePayload(
                    "Test message from Spring Boot on connection open",
                    Instant.now().toString()
                )
            );
        }
    }

        private record MessagePayload(String text, String timestamp) {
        }
}
