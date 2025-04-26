package com.example.demo.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple in-memory broker and set destination prefixes
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register the STOMP endpoint at '/ws'
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:4200")  // allow Angular dev origin
                // .setAllowedOrigins("http://localhost:4200")      // (Alternative for Spring Boot <=2.3)
                .withSockJS();  // use SockJS fallback (remove this line for pure WebSocket)
    }
}
