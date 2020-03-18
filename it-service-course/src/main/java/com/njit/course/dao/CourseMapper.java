package com.njit.course.dao;

import com.github.pagehelper.Page;
import com.njit.framework.domain.course.CourseBase;
import com.njit.framework.domain.course.ext.CourseInfo;
import com.njit.framework.domain.course.request.CourseListRequest;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author dustdawn
 * @date 2019/12/7 22:52
 */
@Mapper
public interface CourseMapper {
   /**
    * 根据课程Id查询
    * @param id
    * @return
    */
   CourseBase findCourseBaseById(String id);

   Page<CourseBase> findCourseList();

   Page<CourseInfo> findCourseListPage(CourseListRequest courseListRequest);
}
