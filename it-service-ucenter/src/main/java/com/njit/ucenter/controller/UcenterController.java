package com.njit.ucenter.controller;

import com.njit.api.ucenter.UcenterControllerApi;
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
import com.njit.ucenter.service.MenuService;
import com.njit.ucenter.service.OfficeService;
import com.njit.ucenter.service.RoleService;
import com.njit.ucenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author dustdawn
 * @date 2020/3/12 10:33
 */
@RestController
@RequestMapping("/ucenter")
public class UcenterController implements UcenterControllerApi {
    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private OfficeService officeService;

    /**
     * 根据用户账号查询用户信息
     * @param username
     * @return
     */
    @Override
    @GetMapping("/getuserext")
    public MoocUserExt getUserext(@RequestParam("username") String username) {
        return userService.getUserExt(username);
    }

    /**
     * 查询用户数据列表
     * @param page
     * @param size
     * @return
     */
    @Override
    @GetMapping("/user/list/{page}/{size}")
    public QueryResponseResult<MoocUser> findUserList(@PathVariable("page") int page,
                                                      @PathVariable("size") int size,
                                                      UserListRequest userListRequest) {
        return userService.findUserList(page, size, userListRequest);
    }

    /**
     * 添加用户
     * @param moocUser
     * @return
     */
    @Override
    @PostMapping("/user/add")
    public ResponseResult addUser(@RequestBody MoocUser moocUser) {
        return userService.addUser(moocUser);
    }

    /**
     * 修改用户状态
     * @param id
     * @param status
     * @return
     */
    @Override
    @PutMapping("/user/update/{id}/{status}")
    public ResponseResult editUserStatus(@PathVariable("id") String id, @PathVariable("status") String status) {
        return userService.editStatus(id, status);
    }

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    @Override
    @GetMapping("/user/get/{id}")
    public MoocUser getUser(@PathVariable("id") String id) {
        return userService.getUserById(id);
    }

    /**
     * 根据id修改用户信息
     * @param id
     * @param moocUser
     * @return
     */
    @Override
    @PutMapping("/user/update/{id}")
    public ResponseResult updateUser(@PathVariable("id") String id, @RequestBody MoocUser moocUser) {
        return userService.updateUser(id, moocUser);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @Override
    @DeleteMapping("/user/delete/{id}")
    public ResponseResult deleteUser(@PathVariable("id") String id) {
        return userService.deleteUser(id);
    }

    @Override
    @PostMapping("/user/changepw")
    public ResponseResult changePassword(@RequestParam String userId,
                                         @RequestParam String passwordOld,
                                         @RequestParam String passwordNew) {
        return userService.changePassword(userId, passwordOld, passwordNew);
    }

    /**
     * 查询用户所有角色
     * @param id
     * @return
     */
    @Override
    @GetMapping("/user/roleList/{id}")
    public UserRoleResult getRoleListByUserId(@PathVariable("id") String id) {
        return userService.getRoleListByUserId(id);
    }

    /**
     * 用户分配角色
     * @param userId
     * @param roleIds
     * @return
     */
    @Override
    @PutMapping({"/user/assign/{userId}/{roleId}","/user/assign/{userId}/"})
    public ResponseResult assignRole(@PathVariable("userId") String userId,
                                     @PathVariable(value = "roleId", required = false) String roleIds) {
        return userService.assignRole(userId, roleIds);
    }


    /**
     * 查询角色数据列表
     * @return
     */
    @Override
    @GetMapping("/role/list")
    public QueryResponseResult<MoocRoleAuth> findRoleList() {
        return roleService.findRoleAuthList();
    }

    /**
     * 角色添加
     * @param moocRole
     * @return
     */
    @Override
    @PostMapping("/role/add")
    public ResponseResult addRole(@RequestBody MoocRole moocRole) {
        return roleService.addRole(moocRole);
    }

    /**
     * 根据id获取角色信息
     * @param id
     * @return
     */
    @Override
    @GetMapping("/role/get/{id}")
    public MoocRole getRoleById(@PathVariable("id") String id) {
        return roleService.getRoleById(id);
    }

    /**
     * 修改角色信息
     * @param id
     * @param moocRole
     * @return
     */
    @Override
    @PutMapping("/role/update/{id}")
    public ResponseResult updateRole(@PathVariable("id") String id, @RequestBody MoocRole moocRole) {
        return roleService.updateRole(id, moocRole);
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @Override
    @DeleteMapping("/role/delete/{id}")
    public ResponseResult deleteRole(@PathVariable("id") String id) {
        return roleService.deleteRole(id);
    }

    @Override
    @PutMapping({"/role/assign/{roleId}/{menuId}","/role/assign/{roleId}/"})
    public ResponseResult assignMenu(@PathVariable("roleId") String roleId,
                                     @PathVariable(value = "menuId", required = false) String menuIds) {
        return roleService.assignMenu(roleId, menuIds);
    }

    /**
     * 角色撤销权限
     * @param roleId
     * @param menuId
     * @return
     */
    @Override
    @PutMapping("/role/revoke/{roleId}/{menuId}")
    public ResponseResult revokeMenu(@PathVariable("roleId") String roleId, @PathVariable("menuId") String menuId) {
        return roleService.revokeMenu(roleId, menuId);
    }

    /**
     * 查询菜单数据列表（树形）
     * @return
     */
    @Override
    @GetMapping("/menu/node")
    public MoocMenuNode findMenuNodeList() {
        return menuService.findMenuNodeList();
    }

    /**
     * 查询菜单数据列表
     * @return
     */
    @Override
    @GetMapping("/menu/list")
    public List<MoocMenu> findMenuList(){
        return menuService.findMenuList();
    }

    /**
     * 查询组织列表
     * @return
     */
    @Override
    @GetMapping("/office/list")
    public List<MoocOffice> findOfficeList() {
        return officeService.findOfficeList();
    }

    @Override
    @GetMapping("/office/get/{officeId}")
    public MoocOffice getOfficeById(@PathVariable("officeId") String officeId) {
        return officeService.getOfficeById(officeId);
    }

}
