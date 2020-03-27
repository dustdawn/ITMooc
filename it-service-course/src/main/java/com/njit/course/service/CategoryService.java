package com.njit.course.service;

import com.njit.course.dao.CategoryMapper;
import com.njit.framework.domain.course.ext.CategoryNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dustdawn
 * @date 2020/3/26 21:15
 */
@Service
public class CategoryService {
    @Autowired(required = false)
    private CategoryMapper categoryMapper;
    /**
     * 查询分类列表
     * @return
     */
    public CategoryNode findCategoryList() {
        return categoryMapper.findList();
    }
}
