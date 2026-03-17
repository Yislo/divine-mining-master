package com.divine.common.web.websocket;

import com.divine.common.core.domain.dto.LoginUser;
import com.divine.common.satoken.utils.LoginHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

public class WebSocketAuthInterceptor extends HttpSessionHandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest req = servletRequest.getServletRequest();
            String token = req.getParameter("token");
            LoginUser loginUser = LoginHelper.getLoginUser(token);
            if (loginUser == null) {
                return false;
            }
            // ⭐ 存到 WebSocketSession
            attributes.put("userId", loginUser.getUserId());
            attributes.put("loginUser", loginUser);
        }

        return true;
    }
}
