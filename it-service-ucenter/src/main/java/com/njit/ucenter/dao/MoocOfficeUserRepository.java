package com.njit.ucenter.dao;

import com.njit.framework.domain.ucenter.MoocOfficeUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dustdawn
 * @date 2020/4/10 15:05
 */
public interface MoocOfficeUserRepository extends JpaRepository<MoocOfficeUser, String> {
    /**
     * 根据用户id查询公司所属信息
     * @param userId
     * @return
     */
    MoocOfficeUser findByUserId(String userId);
}
