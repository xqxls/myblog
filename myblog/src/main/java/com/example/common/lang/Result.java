package com.example.common.lang;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description:统一的结果返回
 * @Author: huzhuo
 * @Date: Created in 2021/4/28 15:52
 */

@Data
public class Result implements Serializable {
    //0成功，-1失败
    private int status;
    private String msg;
    private Object data;
    private String action;

    public static Result success() {
        return Result.success("操作成功", null);
    }

    public static Result success(Object data) {
        return Result.success("操作成功", data);
    }

    public static Result success(String msg, Object data) {
        Result result = new Result();
        result.status = 0;
        result.msg = msg;
        result.data = data;
        return result;
    }

    public static Result fail(String msg) {
        Result result = new Result();
        result.status = -1;
        result.msg = msg;
        result.data = null;
        return result;
    }

    public Result action(String action) {
        this.action = action;
        return this;
    }
}
