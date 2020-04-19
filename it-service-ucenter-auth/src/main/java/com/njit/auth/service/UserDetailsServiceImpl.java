package com.njit.auth.service;

import com.njit.auth.client.UserClient;
import com.njit.framework.domain.ucenter.MoocMenu;
import com.njit.framework.domain.ucenter.ext.MoocUserExt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限和用户的建立
 * SpringSecurity为我们提供了两个非常有用的接口：
 *
 * UserDetails-提供用户核心信息
 * GrantedAuthority-授予身份验证对象以权限
 *
 * Spring Security配置
 * @author dustdawn
 * @date 2020/4/11 13:11
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    ClientDetailsService clientDetailsService;
    @Autowired
    UserClient userClient;

    /**
     * 负责存取数据库用户核心信息，返回带权限的用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 获取当前登录用户信息
        //取出身份，如果身份为空说明没有认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //没有认证统一采用http basic认证，http basic中存储了client_id和client_secret
        if(authentication == null){
            // 从oauth_client_details表中获取client的信息，进行Http Basic认证
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
            if(clientDetails!=null){
                //密码
                String clientSecret = clientDetails.getClientSecret();
                return new User(username,clientSecret,AuthorityUtils.commaSeparatedStringToAuthorityList(""));
            }
        }
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        //远程调用用户中心根据账号查询用户信息
        MoocUserExt userExt = userClient.getUserExt(username);
        if (userExt == null) {
            //返回为空给spring security表示用户不存在
            return null;
        }


        //取出正确密码（hash值）
        String password = userExt.getPassword();

        //用户权限，这里暂时使用静态数据，最终会从数据库读取
        //从数据库获取权限
        List<MoocMenu> permissions = userExt.getPermissions();
        if (permissions == null) {
            permissions = new ArrayList<>();
        }
        List<String> user_permission = new ArrayList<>();
        permissions.forEach(item-> user_permission.add(item.getCode()));

        String user_permission_string  = StringUtils.join(user_permission.toArray(), ",");
        // 将username，password，授权信息封装到springsecurity框架的user对象中
        UserJwt userDetails = new UserJwt(username,
                password,
                AuthorityUtils.commaSeparatedStringToAuthorityList(user_permission_string));
        userDetails.setId(userExt.getId());
        userDetails.setOfficeId(userExt.getOfficeId());//所属组织
        userDetails.setName(userExt.getName());//用户名称


        return userDetails;
    }
}
