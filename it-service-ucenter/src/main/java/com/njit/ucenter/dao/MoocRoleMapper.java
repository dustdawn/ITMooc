package com.njit.ucenter.dao;

import com.njit.framework.domain.ucenter.ext.MoocRoleAuth;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author dustdawn
 * @date 2020/4/14 0:25
 */
@Mapper
public interface MoocRoleMapper {
    /**
     * 查询角色数据列表，且包含权限
     * @return
     */
    public List<MoocRoleAuth> findRoleAuthList();
}
