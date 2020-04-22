package com.njit.auth.client;

import com.njit.framework.client.MoocServiceList;
import com.njit.framework.domain.ucenter.MoocUser;
import com.njit.framework.domain.ucenter.ext.MoocUserExt;
import com.njit.framework.model.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户中心服务Feign接口调用
 * @author dustdawn
 * @date 2020/3/12 10:45
 */
@FeignClient(value = MoocServiceList.MOOC_SERVICE_UCENTER)
public interface UserClient {
    /**
     * 根据账户查询用户信息
     * @param username
     * @return
     */
    @GetMapping("/ucenter/getuserext")
    public MoocUserExt getUserExt(@RequestParam("username") String username);

    @PostMapping("/ucenter/user/add")
    public ResponseResult addUser(@RequestBody MoocUser moocUser);

}
