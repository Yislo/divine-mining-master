package com.divine.common.web.websocket;

import com.divine.common.core.domain.dto.LoginUser;
import com.divine.common.satoken.utils.LoginHelper;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@Component
public class WebSocketAuthInterceptor extends HttpSessionHandshakeInterceptor {

    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request,
                                   @NonNull ServerHttpResponse response,
                                   @NonNull WebSocketHandler wsHandler,
                                   @NonNull Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest req = servletRequest.getServletRequest();
            // 获取身份验证
            String token = req.getParameter("token");
            LoginUser loginUser = LoginHelper.getLoginUser(token);
            if (loginUser == null) {
                return false;
            }
            //存到 WebSocketSession
            attributes.put("userId", loginUser.getUserId());
            attributes.put("loginUser", loginUser);
        }
        return true;
    }
}
