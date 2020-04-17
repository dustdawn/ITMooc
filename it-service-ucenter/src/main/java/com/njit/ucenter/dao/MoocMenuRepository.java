package com.njit.ucenter.dao;

import com.njit.framework.domain.ucenter.MoocMenu;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dustdawn
 * @date 2020/4/16 13:01
 */
public interface MoocMenuRepository extends JpaRepository<MoocMenu, String> {
}
