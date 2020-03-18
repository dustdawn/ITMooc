package com.njit.course.dao;

import com.njit.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author dustdawn
 * @date 2019/12/9 15:48
 */
@Mapper
public interface CategoryMapper {
    CategoryNode findList();
}
