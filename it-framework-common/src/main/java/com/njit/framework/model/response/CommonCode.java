package com.njit.framework.model.response;

import lombok.ToString;

/**
 * 结果集实现类
 * 通用结果集 枚举类
 * @author dustdawn
 * @date 2020/3/15 22:33
 */

@ToString
public enum CommonCode implements ResultCode{
    REQUEST_METHOD_NOT_SUPPORTED(false,10004,"请求方法不支持！"),
    INVALID_PARAM(false,10003,"参数非法！"),
    SUCCESS(true,10000,"操作成功！"),
    FAIL(false,11111,"操作失败！"),
    UNAUTHENTICATED(false,10001,"此操作需要登陆系统！"),
    UNAUTHORIZE(false,10002,"权限不足，无权操作！"),
    SERVER_ERROR(false,99999,"抱歉，系统繁忙，请稍后重试！");


    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;
    private CommonCode(boolean success,int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return success;
    }
    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }


}
