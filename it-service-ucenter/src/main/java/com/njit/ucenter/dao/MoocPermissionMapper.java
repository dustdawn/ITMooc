package com.njit.ucenter.dao;

import com.njit.framework.domain.ucenter.MoocMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author dustdawn
 * @date 2020/4/16 18:42
 */
@Mapper
public interface MoocPermissionMapper {

    public List<MoocMenu> findMenuListByRoleId(String roleId);

}
