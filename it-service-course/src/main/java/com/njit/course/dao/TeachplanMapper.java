package com.njit.course.dao;

import com.njit.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author dustdawn
 * @date 2019/12/8 22:59
 */
@Mapper
public interface TeachplanMapper {
    public TeachplanNode findList(String courseId);

}
