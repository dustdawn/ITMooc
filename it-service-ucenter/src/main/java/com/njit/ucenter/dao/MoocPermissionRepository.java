package com.njit.ucenter.dao;

import com.njit.framework.domain.ucenter.MoocPermission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dustdawn
 * @date 2020/4/16 18:55
 */
public interface MoocPermissionRepository extends JpaRepository<MoocPermission, String> {

    MoocPermission findByRoleIdAndMenuId(String roleId, String menuId);

}
