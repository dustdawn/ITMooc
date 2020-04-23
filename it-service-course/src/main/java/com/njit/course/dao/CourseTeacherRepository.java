package com.njit.course.dao;

import com.njit.framework.domain.course.CourseTeacher;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dustdawn
 * @date 2020/4/23 17:18
 */
public interface CourseTeacherRepository extends JpaRepository<CourseTeacher, String> {
    CourseTeacher findByUserId(String userId);
}
