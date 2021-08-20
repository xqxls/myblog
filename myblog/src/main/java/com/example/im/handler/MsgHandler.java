package com.example.im.handler;

import org.tio.core.ChannelContext;
import org.tio.websocket.common.WsRequest;

/**
 * @Description:消息处理接口
 * @Author: huzhuo
 * @Date: Created in 2021/5/8 22:49
 */

public interface MsgHandler {
    void handler(String data, WsRequest wsRequest, ChannelContext channelContext);
}
