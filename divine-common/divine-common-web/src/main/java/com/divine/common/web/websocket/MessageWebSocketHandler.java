package com.divine.common.web.websocket;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
public class MessageWebSocketHandler extends TextWebSocketHandler {

    // 存放当前在线的会话，Key 为 UserId
    private static final Map<Long, CopyOnWriteArraySet<WebSocketSession>> SESSION_POOL = new ConcurrentHashMap<>();

    /**
     * 连接建立成功
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long userId = getUserId(session);
        if (userId != null) {
            SESSION_POOL
                .computeIfAbsent(userId, k -> new CopyOnWriteArraySet<>())
                .add(session);
            log.info("用户 {} 建立连接", userId);
        }
    }

    /**
     * 收到前端消息（如果只是后端单向推送，此方法可以空着）
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.info("收到来自客户端的消息: {}", message.getPayload());
    }

    /**
     * 连接关闭
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = getUserId(session);
        if (userId != null) {
            CopyOnWriteArraySet<WebSocketSession> sessions = SESSION_POOL.get(userId);
            if (sessions != null) {
                sessions.remove(session);
                if (sessions.isEmpty()) {
                    SESSION_POOL.remove(userId);
                }
            }
            log.info("用户 {} 断开连接", userId);
        }
    }

    /**
     * 推送消息给指定用户
     */
    public void sendMessage(Long userId, WsMessage message) {
        CopyOnWriteArraySet<WebSocketSession> sessions = SESSION_POOL.get(userId);
        if (sessions == null) {
            log.error("消息推送失败，用户：{}已离线", userId);
            return;
        }
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(JSON.toJSONString(message)));
                } catch (IOException e) {
                    log.error("推送失败", e);
                }
            }
        }
    }

    private Long getUserId(WebSocketSession session) {
        return (Long) session.getAttributes().get("userId");
    }
}
