package com.example.im.message;

import com.example.im.vo.ImTo;
import com.example.im.vo.ImUser;
import lombok.Data;

/**
 * @Description:输入的消息
 * @Author: huzhuo
 * @Date: Created in 2021/5/8 22:49
 */

@Data
public class ChatImMess {

    private ImUser mine;
    private ImTo to;

}
