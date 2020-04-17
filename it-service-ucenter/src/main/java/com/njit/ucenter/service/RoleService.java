package com.njit.ucenter.service;

import com.njit.framework.domain.ucenter.MoocMenu;
import com.njit.framework.domain.ucenter.MoocPermission;
import com.njit.framework.domain.ucenter.MoocRole;
import com.njit.framework.domain.ucenter.ext.MoocRoleAuth;
import com.njit.framework.domain.ucenter.response.UcenterCode;
import com.njit.framework.exception.CustomException;
import com.njit.framework.model.response.CommonCode;
import com.njit.framework.model.response.QueryResponseResult;
import com.njit.framework.model.response.QueryResult;
import com.njit.framework.model.response.ResponseResult;
import com.njit.ucenter.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author dustdawn
 * @date 2020/4/14 0:36
 */
@Service
public class RoleService {
    @Autowired(required = false)
    private MoocRoleMapper moocRoleMapper;
    @Autowired
    private MoocRoleRepository moocRoleRepository;
    @Autowired
    private MoocUserRoleRepository moocUserRoleRepository;
    @Autowired(required = false)
    private MoocPermissionMapper moocPermissionMapper;
    @Autowired
    private MoocPermissionRepository moocPermissionRepository;
    @Autowired
    private MoocMenuRepository moocMenuRepository;

    /**
     * 查询角色列表
     * @return
     */
    public QueryResponseResult findRoleAuthList() {
        List<MoocRoleAuth> roleAuthList = moocRoleMapper.findRoleAuthList();
        QueryResult queryResult = new QueryResult();
        queryResult.setList(roleAuthList);
        queryResult.setTotal(roleAuthList.size());
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);

    }

    /**
     * 角色添加
     * @param moocRole
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public ResponseResult addRole(MoocRole moocRole) {
        if (moocRole == null) {
            return new ResponseResult(CommonCode.INVALID_PARAM);
        }
        moocRole.setCreate_time(new Date());
        moocRoleRepository.save(moocRole);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 根据id查询角色信息
     * @param id
     * @return
     */
    public MoocRole getRoleById(String id) {
        Optional<MoocRole> roleOptional = moocRoleRepository.findById(id);
        return roleOptional.orElse(null);
    }

    /**
     * 修改角色
     * @param id
     * @param moocRole
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public ResponseResult updateRole(String id, MoocRole moocRole) {
        MoocRole role = this.getRoleById(id);
        if (role == null) {
            return new ResponseResult(UcenterCode.ROLE_NOT_EXISTS);
        }
        if (StringUtils.isNotEmpty(moocRole.getRoleName())) {
            role.setRoleName(moocRole.getRoleName());
        }
        if (StringUtils.isNotEmpty(moocRole.getRoleCode())) {
            role.setRoleCode(moocRole.getRoleCode());
        }
        if (StringUtils.isNotEmpty(moocRole.getDescription())) {
            role.setDescription(moocRole.getDescription());
        }
        role.setUpdateTime(new Date());
        moocRoleRepository.save(role);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public ResponseResult deleteRole(String id) {
        MoocRole role = this.getRoleById(id);
        if (role == null) {
            return new ResponseResult(UcenterCode.ROLE_NOT_EXISTS);
        }
        moocUserRoleRepository.deleteByRoleId(id);
        moocRoleRepository.delete(role);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 角色授予权限
     * @param roleId
     * @param menuIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult assignMenu(String roleId, String menuIds) {
        if (StringUtils.isEmpty(menuIds)) {
            menuIds = "";
        }
        String[] menus = menuIds.split(",");
        // 待删除菜单
        List<String> menuDel = new ArrayList<>();
        // 待添加菜单
        List<String> menuAdd = new ArrayList<>();
        // 前端请求菜单
        List<String> menuRequest = new ArrayList<>();
        // 保存已有菜单的id
        List<String> menuList = new ArrayList<>();

        // 查询角色所有的权限
        List<MoocMenu> menuListByRoleId = moocPermissionMapper.findMenuListByRoleId(roleId);
        // 添加已有权限的id
        menuListByRoleId.forEach( moocMenu -> {
            menuList.add(moocMenu.getId());
        });

        for (int i = 0; i < menus.length; i++) {
            // 添加需要分配的角色Id
            menuRequest.add(menus[i]);
        }

        // 将数据库中已有角色Id中与最新的权限Id做比对，取出最新权限Id中没有的项，作为删除的权限
        menuDel = menuList.stream()
                .filter((String s) -> !menuRequest.contains(s))
                .collect(Collectors.toList());

        // 从最新的权限Id中，过滤掉数据库中已有的角色Id，作为添加的权限
        menuAdd = menuRequest.stream()
                .filter((String r) -> !menuList.contains(r) && StringUtils.isNotEmpty(r))
                .collect(Collectors.toList());




        if (menuAdd.size() > 0) {
            List<MoocPermission> permissionList = new ArrayList<>();
            // 进行权限添加
            for (int i = 0; i < menuAdd.size(); i++) {
                MoocPermission permission = new MoocPermission();
                permission.setRoleId(roleId);
                permission.setMenuId(menuAdd.get(i));
                permission.setCreateTime(new Date());
                permissionList.add(permission);
            }
            moocPermissionRepository.saveAll(permissionList);
        }
        if (menuDel.size() > 0) {
            List<MoocPermission> permissionList = new ArrayList<>();
            // 进行权限删除
            for (int i = 0; i < menuDel.size(); i++) {
                MoocPermission permission = moocPermissionRepository.findByRoleIdAndMenuId(roleId, menuDel.get(i));
                permissionList.add(permission);
            }
            moocPermissionRepository.deleteAll(permissionList);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 角色撤销权限
     * @param roleId
     * @param menuId
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public ResponseResult revokeMenu(String roleId, String menuId) {
        MoocPermission permission = moocPermissionRepository.findByRoleIdAndMenuId(roleId, menuId);
        if (permission == null) {
            return new ResponseResult(CommonCode.INVALID_PARAM);
        }
        moocPermissionRepository.delete(permission);
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
