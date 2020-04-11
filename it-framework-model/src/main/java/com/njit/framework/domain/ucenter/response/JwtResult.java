package com.njit.framework.domain.ucenter.response;

import com.njit.framework.model.response.ResponseResult;
import com.njit.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author dustdawn
 * @date 2020/4/11 16:35
 */
@Data
@ToString
@NoArgsConstructor
public class JwtResult extends ResponseResult {
    private String jwt;

    public JwtResult(ResultCode resultCode, String jwt) {
        super(resultCode);
        this.jwt = jwt;
    }

}
