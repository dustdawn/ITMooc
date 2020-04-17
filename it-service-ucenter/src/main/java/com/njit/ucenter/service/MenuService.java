package com.njit.ucenter.service;

import com.njit.framework.domain.ucenter.MoocMenu;
import com.njit.framework.domain.ucenter.ext.MoocMenuNode;
import com.njit.ucenter.dao.MoocMenuMapper;
import com.njit.ucenter.dao.MoocMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dustdawn
 * @date 2020/4/13 19:30
 */
@Service
public class MenuService {
    @Autowired(required = false)
    private MoocMenuMapper moocMenuMapper;
    @Autowired
    private MoocMenuRepository moocMenuRepository;
    /**
     * .查询菜单列表
     * @return
     */
    public List<MoocMenu> findMenuList() {
        Sort sort = new Sort(Sort.Direction.ASC, "level","sort");
        return moocMenuRepository.findAll();
    }

    /**
     * 查询菜单树形列表
     * @return
     */
    public MoocMenuNode findMenuNodeList() {
        return moocMenuMapper.findMenuList();
    }
}
