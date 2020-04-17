package com.njit.ucenter.dao;

import com.njit.framework.domain.ucenter.MoocRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author dustdawn
 * @date 2020/4/15 15:10
 */
@Mapper
public interface MoocUserRoleMapper {
    /**
     * 通过userId查询角色列表
     * @param userId
     * @return
     */
    public List<MoocRole> findRoleListByUserId(String userId);
}
