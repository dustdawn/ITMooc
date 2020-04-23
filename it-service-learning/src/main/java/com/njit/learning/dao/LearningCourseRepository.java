package com.njit.learning.dao;

import com.njit.framework.domain.learning.LearningCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dustdawn
 * @date 2020/4/4 12:16
 */
public interface LearningCourseRepository extends JpaRepository<LearningCourse, String> {
    /**
     * 根据用户id和课程id查询
     * @param userId
     * @param courseId
     * @return
     */
    LearningCourse findByUserIdAndCourseId(String userId,String courseId);

    /**
     * 通过用户id查询所有课程列表
     * @param userId
     * @return
     */
    Page<LearningCourse> findByUserId(Pageable pageable, String userId);
}
