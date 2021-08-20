package com.example.im.handler;

import com.example.common.lang.Consts;
import com.example.im.handler.impl.ChatMsgHandler;
import com.example.im.handler.impl.PingMsgHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:消息处理工厂
 * @Author: huzhuo
 * @Date: Created in 2021/5/8 22:49
 */

@Slf4j
public class MsgHandlerFactory {

    private static Map<String, MsgHandler> handlerMap = new HashMap<>();

    public static void init() {
        handlerMap.put(Consts.IM_MESS_TYPE_CHAT, new ChatMsgHandler());
        handlerMap.put(Consts.IM_MESS_TYPE_PING, new PingMsgHandler());

        log.info("hander factory init!!");
    }

    public static MsgHandler getMsgHandler(String type) {
        return handlerMap.get(type);
    }

}
