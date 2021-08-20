package com.example.im.message;

import com.example.im.vo.ImMess;
import lombok.Data;

/**
 * @Description:输出的消息
 * @Author: huzhuo
 * @Date: Created in 2021/5/8 22:49
 */

@Data
public class ChatOutMess {

    private String emit;
    private ImMess data;

}
