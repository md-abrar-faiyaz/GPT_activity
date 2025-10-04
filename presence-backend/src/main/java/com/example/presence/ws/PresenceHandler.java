package com.example.presence.ws;

import com.example.presence.model.AppStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CopyOnWriteArrayList;

public class PresenceHandler extends TextWebSocketHandler {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    // Single shared in-memory status:
    private static AppStatus currentStatus = new AppStatus(false, null, null);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        // send current status immediately to new client:
        session.sendMessage(new TextMessage(mapper.writeValueAsString(currentStatus)));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Expect message payload like: {"user":"Alice","active":true}
        try {
            AppStatus incoming = mapper.readValue(message.getPayload(), AppStatus.class);
            // update current status with timestamp
            Instant instant = Instant.now();
            ZoneId local = ZoneId.systemDefault();
            ZonedDateTime localtime = instant.atZone(local);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
            currentStatus = new AppStatus(
                    incoming.isActive(),
                    incoming.getUser(),
                    localtime.format(formatter)
            );
            broadcastStatus();
        } catch (Exception e) {
            // ignore malformed payloads in this simple example
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

    private void broadcastStatus() {
        try {
            String json = mapper.writeValueAsString(currentStatus);
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(json));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
