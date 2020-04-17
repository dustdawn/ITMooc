package com.njit.framework.domain.ucenter.response;

import com.njit.framework.domain.ucenter.MoocRole;
import com.njit.framework.model.response.ResponseResult;
import com.njit.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 用户角色响应结构
 * @author dustdawn
 * @date 2020/4/15 14:55
 */
@Data
@ToString
@NoArgsConstructor
public class UserRoleResult extends ResponseResult {
    public UserRoleResult(ResultCode resultCode, List<MoocRole> roleList) {
        super(resultCode);
        this.roleList = roleList;
    }
    private List<MoocRole> roleList;
}
