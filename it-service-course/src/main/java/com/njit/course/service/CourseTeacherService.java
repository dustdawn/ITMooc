package com.njit.course.service;

import com.njit.course.dao.CourseTeacherRepository;
import com.njit.framework.domain.course.CourseTeacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dustdawn
 * @date 2020/4/23 17:16
 */
@Service
public class CourseTeacherService {
    @Autowired
    private CourseTeacherRepository courseTeacherRepository;
    public CourseTeacher getTeacherInfo(String id) {
        return courseTeacherRepository.findByUserId(id);
    }
}
