package com.njit.framework.domain.ucenter.ext;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author dustdawn
 * @date 2020/4/11 16:09
 */
@Data
@ToString
@NoArgsConstructor
public class AuthToken {
    /**
     * 访问token，用户身份令牌jti
     */
    String userJti;
    /**
     * 刷新token
     */
    String refreshToken;
    /**
     * jwt令牌 access_token
     */
    String jwtAccessToken;
}
