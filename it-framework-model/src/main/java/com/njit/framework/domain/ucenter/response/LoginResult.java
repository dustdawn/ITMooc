package com.njit.framework.domain.ucenter.response;

import com.njit.framework.model.response.ResponseResult;
import com.njit.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 登录状态响应结构
 * @author dustdawn
 * @date 2020/4/11 16:33
 */
@Data
@ToString
@NoArgsConstructor
public class LoginResult extends ResponseResult {
    public LoginResult(ResultCode resultCode, String token) {
        super(resultCode);
        this.token = token;
    }
    private String token;
}