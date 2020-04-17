package com.njit.ucenter.dao;

import com.njit.framework.domain.ucenter.MoocUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dustdawn
 * @date 2020/4/14 18:30
 */
public interface MoocUserRoleRepository extends JpaRepository<MoocUserRole, String> {
    /**
     * 通过userId删除关系
     * @param userId
     * @return
     */
    Long deleteByUserId(String userId);

    Long deleteByRoleId(String roleId);

    MoocUserRole findByUserIdAndRoleId(String userId, String roleId);




}
