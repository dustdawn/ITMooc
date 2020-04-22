package com.njit.api.auth;

import com.njit.framework.domain.ucenter.MoocUser;
import com.njit.framework.domain.ucenter.request.LoginRequest;
import com.njit.framework.domain.ucenter.response.JwtResult;
import com.njit.framework.domain.ucenter.response.LoginResult;
import com.njit.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author dustdawn
 * @date 2020/4/11 16:31
 */
@Api(value = "用户认证", description = "用户认证接口")
public interface AuthControllerApi {
    /**
     * 登录
     * @param loginRequest
     * @return
     */
    @ApiOperation("登录")
    public LoginResult login(LoginRequest loginRequest);

    /**
     * 退出登录
     * @return
     */
    @ApiOperation("退出")
    public ResponseResult logout();

    /**
     * 用户注册
     * @param moocUser
     * @return
     */
    @ApiOperation("注册")
    public ResponseResult logon(MoocUser moocUser);

    /**
     * 查询userjwt令牌
     * @return
     */
    @ApiOperation("查询userjwt令牌")
    public JwtResult userjwt();
}