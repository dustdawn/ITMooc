package com.njit.framework.domain.ucenter.request;

import com.njit.framework.model.request.RequestData;
import lombok.Data;
import lombok.ToString;

/**
 * @author dustdawn
 * @date 2020/4/13 18:08
 */
@Data
@ToString
public class UserListRequest extends RequestData {
    /**
     * 用户账号
     */
    private String username;
    /**
     * 用户姓名
     */
    private String name;
}
