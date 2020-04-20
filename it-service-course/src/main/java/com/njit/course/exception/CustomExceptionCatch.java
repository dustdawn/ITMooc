package com.njit.course.exception;

import com.njit.framework.exception.ExceptionCatch;
import com.njit.framework.model.response.CommonCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 课程管理自定义的异常类型，其中定义异常类型所对应的错误代码
 * @author dustdawn
 * @date 2020/3/13 21:40
 */
@ControllerAdvice
public class CustomExceptionCatch extends ExceptionCatch {
    static {
        //org.springframework.security.access.AccessDeniedException: 不允许访问
        builder.put(AccessDeniedException.class, CommonCode.UNAUTHORIZE);
    }
}
