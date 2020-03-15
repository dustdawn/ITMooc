package com.njit.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.njit.framework.model.response.CommonCode;
import com.njit.framework.model.response.ResponseResult;
import com.njit.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author dustdawn
 * @date 2019/11/30 10:01
 *
 * 控制器增强
 */
@ControllerAdvice
public class ExceptionCatch {
    private static final Logger LOGGER = LoggerFactory.getLogger(Exception.class);
    /**
     * 谷歌的ImmutableMap，存进去不可修改，且线程安全
     *  k：异常类  v：异常类型
     */
    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;
    //定义map的builder对象，去构建ImmutableMap

    protected static ImmutableMap.Builder<Class<? extends Throwable>, ResultCode> builder = ImmutableMap.builder();

    /**
     * 捕获CustomException此类
     * @param customException
     * @return
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException customException) {
        LOGGER.error("catch Exception: {}", customException.getMessage());
        ResultCode resultCode = customException.getResultCode();
        return new ResponseResult(resultCode);
    }

    /**
     * 捕获Exception此类异常
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception exception) {
        LOGGER.error("catch Exception: {}", exception.getMessage());
        if (EXCEPTIONS == null) {
            //EXCEPTIONS构建成功
            EXCEPTIONS = builder.build();
        }
        //从EXCEPTIONS中找异常类型所对应得到错误代码，如果找到了将错误代码响应给用户，如果找不到响应9999异常
        ResultCode resultCode = EXCEPTIONS.get(exception.getClass());
        if (null != resultCode) {
            return new ResponseResult(resultCode);
        } else {
            return new ResponseResult(CommonCode.SERVER_ERROR);
        }

    }

    static {
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID_PARAM);
        builder.put(HttpRequestMethodNotSupportedException.class, CommonCode.REQUEST_METHOD_NOT_SUPPORTED);
    }
}
