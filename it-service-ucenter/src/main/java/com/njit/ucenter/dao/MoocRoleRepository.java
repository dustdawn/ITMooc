package com.njit.ucenter.dao;

import com.njit.framework.domain.ucenter.MoocRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dustdawn
 * @date 2020/4/15 15:20
 */
public interface MoocRoleRepository extends JpaRepository<MoocRole, String> {
}
