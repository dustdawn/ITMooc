package com.njit.course.dao;

import com.njit.framework.domain.course.TeachplanMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author dustdawn
 * @date 2020/3/6 22:20
 */
public interface TeachplanMediaRepository extends JpaRepository<TeachplanMedia, String> {
    /**
     * 根据课程id查询列表
     * @param courseId
     * @return
     */
    List<TeachplanMedia> findByCourseId(String courseId);
}
