package com.njit.framework.domain.ucenter.request;

import com.njit.framework.model.request.RequestData;
import lombok.Data;
import lombok.ToString;

/**
 * 登录请求结构
 * @author dustdawn
 * @date 2020/4/11 16:33
 */
@Data
@ToString
public class LoginRequest extends RequestData {

    String username;
    String password;

}