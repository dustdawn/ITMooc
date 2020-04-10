package com.njit.ucenter.controller;

import com.njit.api.ucenter.UcenterControllerApi;
import com.njit.framework.domain.ucenter.ext.MoocUserExt;
import com.njit.ucenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dustdawn
 * @date 2020/3/12 10:33
 */
@RestController
@RequestMapping("/ucenter")
public class UcenterController implements UcenterControllerApi {
    @Autowired
    UserService userService;

    /**
     * 根据用户账号查询用户信息
     * @param username
     * @return
     */
    @Override
    @GetMapping("/getuserext")
    public MoocUserExt getUserext(@RequestParam("username") String username) {
        return userService.getUserExt(username);
    }
}
