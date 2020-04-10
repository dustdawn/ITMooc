package com.njit.ucenter.dao;

import com.njit.framework.domain.ucenter.MoocUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dustdawn
 * @date 2020/4/10 14:53
 */
public interface MoocUserRepository extends JpaRepository<MoocUser, String> {
    /**
     * 根据账号查询用户信息
     * @param username
     * @return
     */
    public MoocUser findByUsername(String username);
}
