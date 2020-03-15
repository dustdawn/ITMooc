package com.njit.framework.exception;

import com.njit.framework.model.response.ResultCode;

/**
 * @author dustdawn
 * @date 2019/11/30 9:59
 */
public class ExceptionCast {
    public static void cast(ResultCode resultCode) {
        throw new CustomException(resultCode);
    }
}
