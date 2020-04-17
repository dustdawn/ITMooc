package com.njit.ucenter.dao;

import com.njit.framework.domain.ucenter.MoocMenu;
import com.njit.framework.domain.ucenter.ext.MoocMenuNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author dustdawn
 * @date 2020/4/10 14:55
 */
@Mapper
public interface MoocMenuMapper {
    /**
     * 根据用户id查询用户的权限
     * @param userId
     * @return
     */
    public List<MoocMenu> selectPermissionByUserId(String userId);

    /**
     * 查询菜单结点
     * @return
     */
    public MoocMenuNode findMenuList();
}
