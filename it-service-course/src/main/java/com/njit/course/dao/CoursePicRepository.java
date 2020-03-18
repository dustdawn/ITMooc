package com.njit.course.dao;

import com.njit.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dustdawn
 * @date 2019/12/11 23:16
 */
public interface CoursePicRepository extends JpaRepository<CoursePic, String> {
    long deleteByCourseid(String courseId);
}
