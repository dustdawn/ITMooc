package com.njit.framework.domain.learning;

import com.njit.framework.model.response.ResultCode;
import lombok.ToString;

/**
 * @author dustdawn
 * @date 2020/3/8 22:33
 */
@ToString
public enum LearningCode implements ResultCode {
    LEARNING_GETMEDIA_ERROR(false, 23001, "获取学习地址失败");
    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;

    private LearningCode(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return false;
    }

    @Override
    public int code() {
        return 0;
    }

    @Override
    public String message() {
        return null;
    }
}
