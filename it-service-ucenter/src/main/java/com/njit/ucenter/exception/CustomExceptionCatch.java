package com.njit.ucenter.exception;

import com.njit.framework.domain.ucenter.response.UcenterCode;
import com.njit.framework.exception.ExceptionCatch;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 用户中心自定义异常
 * @author dustdawn
 * @date 2020/4/15 13:45
 */
@ControllerAdvice
public class CustomExceptionCatch extends ExceptionCatch {
    // ConstraintViolationException
    static {
        //org.springframework.dao.DataIntegrityViolationException
        builder.put(DataIntegrityViolationException.class, UcenterCode.USER_ROLE_EXISTS);
    }
}
