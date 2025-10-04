package com.example.presence.config;

import com.example.presence.ws.PresenceHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Allow all origins for dev; restrict this in production
        registry.addHandler(new PresenceHandler(), "/ws/presence")
                .setAllowedOriginPatterns("*");
    }
}
