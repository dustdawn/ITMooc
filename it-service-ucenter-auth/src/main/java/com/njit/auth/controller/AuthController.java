package com.njit.auth.controller;

import com.njit.api.auth.AuthControllerApi;
import com.njit.auth.service.AuthService;
import com.njit.framework.domain.ucenter.ext.AuthToken;
import com.njit.framework.domain.ucenter.request.LoginRequest;
import com.njit.framework.domain.ucenter.response.AuthCode;
import com.njit.framework.domain.ucenter.response.JwtResult;
import com.njit.framework.domain.ucenter.response.LoginResult;
import com.njit.framework.exception.ExceptionCast;
import com.njit.framework.model.response.CommonCode;
import com.njit.framework.model.response.ResponseResult;
import com.njit.framework.util.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author dustdawn
 * @date 2020/3/10 21:15
 */
@RestController
@RequestMapping("/")
public class AuthController implements AuthControllerApi {
    @Autowired
    public AuthService authService;
    @Value("${auth.clientId}")
    String clientId;
    @Value("${auth.clientSecret}")
    String clientSecret;
    @Value("${auth.cookieDomain}")
    String cookieDomain;
    @Value("${auth.cookieMaxAge}")
    int cookieMaxAge;

    /**
     * 登录
     * @param loginRequest
     * @return
     */
    @Override
    @PostMapping("/userlogin")
    public LoginResult login(LoginRequest loginRequest) {
        if (loginRequest == null || StringUtils.isEmpty(loginRequest.getUsername())) {
            ExceptionCast.cast(AuthCode.AUTH_USERNAME_NONE);
        }
        if (loginRequest == null || StringUtils.isEmpty(loginRequest.getPassword())) {
            ExceptionCast.cast(AuthCode.AUTH_PASSWORD_NONE);
        }
        //获取账号密码
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();


        //申请令牌
        AuthToken authToken = authService.login(username, password, clientId, clientSecret);

        //用户身份令牌
        String access_token = authToken.getUserJti();
        //将令牌存储到cookie
        this.saveCookie(access_token);
        return new LoginResult(CommonCode.SUCCESS, access_token);
    }

    /**
     * 退出登录
     * @return
     */
    @Override
    @PostMapping("/userlogout")
    public ResponseResult logout() {
        //取出身份令牌
        String uid = getTokenFormCookie();
        //删除redis中token
        authService.delToken(uid);

        //清除cookie
        this.clearCookie(uid);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 获得jwt令牌
     * @return
     */
    @Override
    @GetMapping("/userjwt")
    public JwtResult userjwt() {
        //取出cookie中的用户身份令牌（短）
        String uid = this.getTokenFormCookie();
        if (uid == null) {
            return new JwtResult(CommonCode.FAIL, null);
        }
        //拿身份令牌从redis中查询jwt令牌
        AuthToken userToken = authService.getUserToken(uid);

        //将jwt令牌返回给用户
        if (userToken != null) {
            String jwt_token = userToken.getJwtAccessToken();
            return new JwtResult(CommonCode.SUCCESS, jwt_token);
        }

        return null;
    }

    //将令牌存储到cookie
    private void saveCookie(String token) {
        // HttpServletResponse response,String domain,String path, String name,String value, int maxAge,
        // boolean httpOnly(设置成false浏览器可以拿到cookie)
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        CookieUtil.addCookie(response, cookieDomain, "/", "uid", token, cookieMaxAge, false);
    }

    //取出cookie中的身份令牌
    private String getTokenFormCookie() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Map<String, String> map = CookieUtil.readCookie(request, "uid");
        if (map != null && map.get("uid") != null) {
            return map.get("uid");
        }
        return null;
    }

    //从cookie中删除token
    private void clearCookie(String token) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        CookieUtil.addCookie(response, cookieDomain, "/", "uid", token, 0, false);
    }
}
