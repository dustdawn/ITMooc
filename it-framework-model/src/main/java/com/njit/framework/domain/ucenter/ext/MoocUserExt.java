package com.njit.framework.domain.ucenter.ext;

import com.njit.framework.domain.ucenter.MoocMenu;
import com.njit.framework.domain.ucenter.MoocUser;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author dustdawn
 * @date 2020/4/10 15:09
 */
@Data
@ToString
public class MoocUserExt extends MoocUser {

    /**
     * 权限信息
     */
    private List<MoocMenu> permissions;

    /**
     * 组织信息
     */
    private String companyId;
}
