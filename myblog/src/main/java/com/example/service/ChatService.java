package com.example.service;

import com.example.im.vo.ImMess;
import com.example.im.vo.ImUser;

import java.util.List;

/**
 * @Description:聊天室接口
 * @Author: huzhuo
 * @Date: Created in 2021/4/12 23:02
 */

public interface ChatService {
    ImUser getCurrentUser();

    void setGroupHistoryMsg(ImMess responseMess);


    List<Object> getGroupHistoryMsg(int count);
}
