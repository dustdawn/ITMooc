package com.njit.ucenter.dao;

import com.njit.framework.domain.ucenter.MoocMenu;
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
}
