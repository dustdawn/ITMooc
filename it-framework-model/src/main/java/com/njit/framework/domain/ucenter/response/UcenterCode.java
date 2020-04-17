package com.njit.framework.domain.ucenter.response;

import com.google.common.collect.ImmutableMap;
import com.njit.framework.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

/**
 * @author dustdawn
 * @date 2020/4/11 16:10
 */
@ToString
public enum UcenterCode implements ResultCode {
    USER_NOT_EXISTS(false,30001,"用户不存在！"),
    USER_ROLE_EXISTS(false,30002,"该目标已存在！"),
    ROLE_NOT_EXISTS(false,30003,"角色不存在！"),
    MENU_NOT_EXISTS(false,30004,"权限不存在！");
    //操作代码
    @ApiModelProperty(value = "操作是否成功", example = "true", required = true)
    boolean success;

    //操作代码
    @ApiModelProperty(value = "操作代码", example = "30001", required = true)
    int code;
    //提示信息
    @ApiModelProperty(value = "操作提示", example = "用户不存在！", required = true)
    String message;
    private UcenterCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }
    private static final ImmutableMap<Integer, UcenterCode> CACHE;

    static {
        final ImmutableMap.Builder<Integer, UcenterCode> builder = ImmutableMap.builder();
        for (UcenterCode commonCode : values()) {
            builder.put(commonCode.code(), commonCode);
        }
        CACHE = builder.build();
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

