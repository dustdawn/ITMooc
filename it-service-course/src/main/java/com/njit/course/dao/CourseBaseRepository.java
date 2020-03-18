package com.njit.course.dao;

import com.njit.framework.domain.course.CourseBase;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dustdawn
 * @date 2019/12/8 22:52
 */
public interface CourseBaseRepository extends JpaRepository<CourseBase,String> {
}
