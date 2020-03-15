package com.njit.framework.exception;

import com.njit.framework.model.response.ResultCode;

/**
 * 自定义异常类型
 * @author dustdawn
 * @date 2019/11/30 9:53
 */
public class CustomException extends RuntimeException{

    ResultCode resultCode;

    public CustomException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
