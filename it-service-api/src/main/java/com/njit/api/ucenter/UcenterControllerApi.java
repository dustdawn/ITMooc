package com.njit.api.ucenter;

import com.njit.framework.domain.ucenter.ext.MoocUserExt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author dustdawn
 * @date 2020/3/12 10:11
 */
@Api(value = "用户中心", description = "用户中心管理")
public interface UcenterControllerApi {
    /**
     * 根据用户账号查询用户信息
     * @param username
     * @return
     */
    @ApiOperation("根据用户账号查询用户信息，包括权限，组织id")
    public MoocUserExt getUserext(String username);
}
