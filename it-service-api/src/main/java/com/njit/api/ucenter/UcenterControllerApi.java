package com.njit.api.ucenter;

import com.njit.framework.domain.ucenter.MoocMenu;
import com.njit.framework.domain.ucenter.MoocOffice;
import com.njit.framework.domain.ucenter.MoocRole;
import com.njit.framework.domain.ucenter.MoocUser;
import com.njit.framework.domain.ucenter.ext.MoocMenuNode;
import com.njit.framework.domain.ucenter.ext.MoocRoleAuth;
import com.njit.framework.domain.ucenter.ext.MoocUserExt;
import com.njit.framework.domain.ucenter.request.UserListRequest;
import com.njit.framework.domain.ucenter.response.UserRoleResult;
import com.njit.framework.model.response.QueryResponseResult;
import com.njit.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @author dustdawn
 * @date 2020/3/12 10:11
 */
@Api(value = "用户中心", description = "用户中心管理")
public interface UcenterControllerApi {
    /**
     * 根据用户账号查询用户信息
     *
     * @param username
     * @return
     */
    @ApiOperation("根据用户账号查询用户信息，包括权限，组织id")
    public MoocUserExt getUserext(String username);

    /**
     * 查询用户数据列表
     *
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("查询用户数据列表")
    public QueryResponseResult<MoocUser> findUserList(int page, int size, UserListRequest userListRequest);

    /**
     * 添加用户
     *
     * @param moocUser
     * @return
     */
    @ApiOperation("添加用户")
    public ResponseResult addUser(MoocUser moocUser);

    /**
     * 修改用户状态
     *
     * @param id
     * @param status
     * @return
     */
    @ApiOperation("修改用户状态")
    public ResponseResult editUserStatus(String id, String status);

    /**
     * 根据id查询用户信息
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id查询用户信息")
    public MoocUser getUser(String id);

    /**
     * 根据id修改用户信息
     *
     * @param id
     * @param moocUser
     * @return
     */
    @ApiOperation("根据id修改用户信息")
    public ResponseResult updateUser(String id, MoocUser moocUser);

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @ApiOperation("删除用户")
    public ResponseResult deleteUser(String id);

    /**
     * 查询用户所有角色
     *
     * @param id
     * @return
     */
    @ApiOperation("查询用户所有角色")
    public UserRoleResult getRoleListByUserId(String id);

    /**
     * 用户分配角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @ApiOperation("用户分配角色")
    public ResponseResult assignRole(String userId, String roleIds);

    /**
     * 查询角色数据列表
     *
     * @return
     */
    @ApiOperation("查询角色数据列表")
    public QueryResponseResult<MoocRoleAuth> findRoleList();

    /**
     * 添加角色
     *
     * @param moocRole
     * @return
     */
    @ApiOperation("添加角色")
    public ResponseResult addRole(MoocRole moocRole);

    /**
     * 根据id获取角色信息
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id获取角色信息")
    public MoocRole getRoleById(String id);

    /**
     * 根据id修改角色
     *
     * @param id
     * @param moocRole
     * @return
     */
    @ApiOperation("根据id修改角色")
    public ResponseResult updateRole(String id, MoocRole moocRole);

    /**
     * 根据id删除角色
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id删除角色")
    public ResponseResult deleteRole(String id);

    /**
     * 角色分配权限
     *
     * @param roleId
     * @param menuIds
     * @return
     */
    @ApiOperation("角色分配权限")
    public ResponseResult assignMenu(String roleId, String menuIds);

    /**
     * 角色撤销权限
     *
     * @param roleId
     * @param menuId
     * @return
     */
    @ApiOperation("角色撤销权限")
    public ResponseResult revokeMenu(String roleId, String menuId);

    /**
     * 查询菜单树形列表
     *
     * @return
     */
    @ApiOperation("查询菜单树形列表")
    public MoocMenuNode findMenuNodeList();

    /**
     * 查询菜单数据列表
     *
     * @return
     */
    @ApiOperation("查询菜单数据列表")
    public List<MoocMenu> findMenuList();

    /**
     * 查询组织列表
     * @return
     */
    @ApiOperation("查询组织列表")
    public List<MoocOffice> findOfficeList();

    /**
     * 根据id查询组织
     * @param officeId
     * @return
     */
    @ApiOperation("查询组织列表")
    public MoocOffice getOfficeById(String officeId);

}
