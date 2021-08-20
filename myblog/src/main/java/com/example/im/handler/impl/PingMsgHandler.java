package com.example.im.handler.impl;

import com.example.im.handler.MsgHandler;
import org.tio.core.ChannelContext;
import org.tio.websocket.common.WsRequest;

/**
 * @Description:心跳
 * @Author: huzhuo
 * @Date: Created in 2021/5/8 22:49
 */

public class PingMsgHandler implements MsgHandler {

    @Override
    public void handler(String data, WsRequest wsRequest, ChannelContext channelContext) {

    }
}
