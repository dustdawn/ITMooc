package com.njit.ucenter.service;

import com.njit.framework.domain.ucenter.MoocMenu;
import com.njit.framework.domain.ucenter.MoocRole;
import com.njit.framework.domain.ucenter.MoocUser;
import com.njit.framework.domain.ucenter.MoocUserRole;
import com.njit.framework.domain.ucenter.ext.MoocUserExt;
import com.njit.framework.domain.ucenter.request.UserListRequest;
import com.njit.framework.domain.ucenter.response.AuthCode;
import com.njit.framework.domain.ucenter.response.UcenterCode;
import com.njit.framework.domain.ucenter.response.UserRoleResult;
import com.njit.framework.exception.CustomException;
import com.njit.framework.exception.ExceptionCast;
import com.njit.framework.model.response.CommonCode;
import com.njit.framework.model.response.QueryResponseResult;
import com.njit.framework.model.response.QueryResult;
import com.njit.framework.model.response.ResponseResult;
import com.njit.ucenter.dao.MoocMenuMapper;
import com.njit.ucenter.dao.MoocUserRepository;
import com.njit.ucenter.dao.MoocUserRoleMapper;
import com.njit.ucenter.dao.MoocUserRoleRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author dustdawn
 * @date 2020/3/12 10:25
 */
@Service
public class UserService {
    @Autowired
    private MoocUserRepository moocUserRepository;
    @Autowired(required = false)
    private MoocMenuMapper moocMenuMapper;
    @Autowired
    private MoocUserRoleRepository moocUserRoleRepository;
    @Autowired(required = false)
    private MoocUserRoleMapper moocUserRoleMapper;

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

        MoocUserExt moocUserExt = new MoocUserExt();
        BeanUtils.copyProperties(moocUser, moocUserExt);

        //设置权限
        moocUserExt.setPermissions(xcMenus);
        return moocUserExt;
    }

    public QueryResponseResult<MoocUser> findUserList(int page, int size, UserListRequest userListRequest) {
        if (userListRequest == null) {
            userListRequest = new UserListRequest();
        }

        MoocUser user = new MoocUser();
        //定义条件匹配器，模糊查询
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());

        if (StringUtils.isNotEmpty(userListRequest.getUsername())) {
            user.setUsername(userListRequest.getUsername());
        }
        if (StringUtils.isNotEmpty(userListRequest.getName())) {
            user.setName(userListRequest.getName());
        }
        Example<MoocUser> userExample = Example.of(user, exampleMatcher);
        //分页参数
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        Sort sort = new Sort(Sort.Direction.ASC, "username");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<MoocUser> all = moocUserRepository.findAll(userExample, pageable);

        QueryResult<MoocUser> queryResult = new QueryResult<>();
        queryResult.setList(all.getContent());
        queryResult.setTotal(all.getTotalElements());

        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }

    /**
     * 添加用户
     * @param moocUser
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public ResponseResult addUser(MoocUser moocUser) {
        if (moocUser == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        // 对密码进行加密
        String encode = bCryptPasswordEncoder.encode(moocUser.getPassword());
        moocUser.setPassword(encode);
        moocUser.setCreateTime(new Date());
        moocUser.setStatus("1");
        // 设置课程状态为制作中
        moocUserRepository.save(moocUser);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 修改用户状态
     * @param id
     * @param status
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public ResponseResult editStatus(String id, String status) {
        MoocUser user = this.getUserById(id);
        if (user == null) {
            return new ResponseResult(UcenterCode.USER_NOT_EXISTS);
        }
        user.setStatus(status);
        moocUserRepository.save(user);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    public MoocUser getUserById(String id) {
        Optional<MoocUser> userOptional = moocUserRepository.findById(id);
        return userOptional.orElse(null);
    }

    /**
     * 修改用户信息
     * @param id
     * @param moocUser
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public ResponseResult updateUser(String id, MoocUser moocUser) {
        MoocUser user = this.getUserById(id);
        if (user == null) {
            return new ResponseResult(UcenterCode.USER_NOT_EXISTS);
        }
        if (StringUtils.isNotEmpty(moocUser.getName())) {
            user.setName(moocUser.getName());
        }
        if (StringUtils.isNotEmpty(moocUser.getEmail())) {
            user.setEmail(moocUser.getEmail());
        }
        if (StringUtils.isNotEmpty(moocUser.getMobile())) {
            user.setMobile(moocUser.getMobile());
        }
        if (StringUtils.isNotEmpty(moocUser.getOfficeId())) {
            user.setOfficeId(moocUser.getOfficeId());
        }
        user.setUpdateTime(new Date());
        moocUserRepository.save(user);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public ResponseResult deleteUser(String id) {
        MoocUser user = this.getUserById(id);
        if (user == null) {
            return new ResponseResult(UcenterCode.USER_NOT_EXISTS);
        }
        moocUserRoleRepository.deleteByUserId(id);
        moocUserRepository.delete(user);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 查询用户所有角色
     * @param id
     * @return
     */
    public UserRoleResult getRoleListByUserId(String id) {
        MoocUser user = this.getUserById(id);
        if (user == null) {
            return new UserRoleResult(UcenterCode.USER_NOT_EXISTS, null);
        }
        List<MoocRole> roleListByUserId = moocUserRoleMapper.findRoleListByUserId(id);
        return new UserRoleResult(CommonCode.SUCCESS, roleListByUserId);
    }

    /**
     * 用户分配权限
     * @param userId
     * @param roleIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult assignRole(String userId, String roleIds) {
        if (StringUtils.isEmpty(roleIds)) {
            roleIds = "";
        }
        String[] roles = roleIds.split(",");
        List<String> roleDel = new ArrayList<>();
        List<String> roleAdd = new ArrayList<>();
        List<String> roleRequest = new ArrayList<>();
        List<String> roleList = new ArrayList<>();

        // 查询用户所有的角色
        List<MoocRole> roleListByUserId = moocUserRoleMapper.findRoleListByUserId(userId);
        // 添加已有角色的id
        roleListByUserId.forEach( moocRole -> {
            roleList.add(moocRole.getId());
        });

        for (int i = 0; i < roles.length; i++) {
            // 添加需要分配的角色Id
            roleRequest.add(roles[i]);
        }

        // 将数据库中已有角色Id中与最新的权限Id做比对，取出最新权限Id中没有的项，作为删除的权限
        roleDel = roleList.stream()
                .filter((String s) -> !roleRequest.contains(s))
                .collect(Collectors.toList());

        // 从最新的权限Id中，过滤掉数据库中已有的角色Id，作为添加的权限
        roleAdd = roleRequest.stream()
                .filter((String r) -> !roleList.contains(r) && StringUtils.isNotEmpty(r))
                .collect(Collectors.toList());




        if (roleAdd.size() > 0) {
            List<MoocUserRole> userRoleList = new ArrayList<>();
            // 进行权限添加
            for (int i = 0; i < roleAdd.size(); i++) {
                MoocUserRole userRole = new MoocUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleAdd.get(i));
                userRole.setCreateTime(new Date());
                userRoleList.add(userRole);
            }
            moocUserRoleRepository.saveAll(userRoleList);
        }
        if (roleDel.size() > 0) {
            List<MoocUserRole> userRoleList = new ArrayList<>();
            // 进行权限删除
            for (int i = 0; i < roleDel.size(); i++) {
                MoocUserRole userRole = moocUserRoleRepository.findByUserIdAndRoleId(userId, roleDel.get(i));
                userRoleList.add(userRole);
            }
            moocUserRoleRepository.deleteAll(userRoleList);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 修改密码
     * @param userId
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public ResponseResult changePassword(String userId, String passwordOld, String passwordNew) {
        Optional<MoocUser> userOptional = moocUserRepository.findById(userId);
        if (userOptional == null) {
            return new ResponseResult(UcenterCode.USER_NOT_EXISTS);
        }
        MoocUser user = userOptional.get();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matches = bCryptPasswordEncoder.matches(passwordOld, user.getPassword());
        if (!matches) {
            return new ResponseResult(AuthCode.AUTH_CREDENTIAL_ERROR);
        }
        user.setPassword(bCryptPasswordEncoder.encode(passwordNew));
        moocUserRepository.save(user);
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
