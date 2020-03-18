package com.njit.course.dao;

import com.njit.framework.domain.course.TeachplanMediaPub;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dustdawn
 * @date 2020/3/6 22:20
 */
public interface TeachplanMediaPubRepository extends JpaRepository<TeachplanMediaPub, String> {
    /**
     * 根据课程id删除列表
     * @param courseId
     * @return 大于0执行成功
     */
    Long deleteByCourseId(String courseId);
}
