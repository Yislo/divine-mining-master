package com.divine.common.web.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WsMessage {

    /**
     * 消息类型
     */
    private String type;

    /**
     * 数据
     */
    private Object data;
}
