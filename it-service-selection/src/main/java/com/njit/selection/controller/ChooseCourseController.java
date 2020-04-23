package com.njit.selection.controller;

import com.njit.framework.model.response.ResponseResult;
import com.njit.selection.service.ChooseCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dustdawn
 * @date 2020/4/23 14:38
 */
@RestController
@RequestMapping("/selection")
public class ChooseCourseController {
    @Autowired
    private ChooseCourseService chooseCourseService;

    @PostMapping("/choose/{userId}/{courseId}")
    public ResponseResult chooseCourse(@PathVariable("userId") String userId, @PathVariable("courseId") String courseId) {
       return  chooseCourseService.chooseCourse(userId, courseId);
    }
}
