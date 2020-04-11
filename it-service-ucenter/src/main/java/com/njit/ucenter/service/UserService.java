package com.njit.ucenter.service;

import com.njit.framework.domain.ucenter.MoocMenu;
import com.njit.framework.domain.ucenter.MoocOfficeUser;
import com.njit.framework.domain.ucenter.MoocUser;
import com.njit.framework.domain.ucenter.ext.MoocUserExt;
import com.njit.ucenter.dao.MoocMenuMapper;
import com.njit.ucenter.dao.MoocOfficeUserRepository;
import com.njit.ucenter.dao.MoocUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dustdawn
 * @date 2020/3/12 10:25
 */
@Service
public class UserService {
    @Autowired
    MoocUserRepository moocUserRepository;
    @Autowired
    MoocOfficeUserRepository moocOfficeUserRepository;
    @Autowired(required = false)
    MoocMenuMapper moocMenuMapper;

    /**
     * 根据账号查询用户基本信息
     * @param username
     * @return
     */
    public MoocUser findXcUserByUsername(String username) {
        return moocUserRepository.findByUsername(username);
    }

    //根据账号查询用户信息
    public MoocUserExt getUserExt(String username) {
        //根据账号查询基本信息（XcUser）
        MoocUser moocUser = this.findXcUserByUsername(username);
        if (moocUser == null) {
            return null;
        }
        //用户id
        String userId = moocUser.getId();
        //查询用户所有权限
        List<MoocMenu> xcMenus = moocMenuMapper.selectPermissionByUserId(userId);
        //根据用户id查询用户所属公司id
        MoocOfficeUser moocOfficeUser = moocOfficeUserRepository.findByUserId(userId);
        //取用户公司id
        String officeId = null;
        if (moocOfficeUser != null) {
            officeId = moocOfficeUser.getCompanyId();
        }
        MoocUserExt moocUserExt = new MoocUserExt();
        BeanUtils.copyProperties(moocUser, moocUserExt);
        moocUserExt.setOfficeId(officeId);
        //设置权限
        moocUserExt.setPermissions(xcMenus);
        return moocUserExt;
    }
}
