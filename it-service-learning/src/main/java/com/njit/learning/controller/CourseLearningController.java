package com.njit.learning.controller;

import com.njit.api.learning.CourseLearningControllerApi;
import com.njit.framework.domain.learning.response.GetMediaResult;
import com.njit.learning.service.LearningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dustdawn
 * @date 2020/4/4 12:25
 */
@RestController
@RequestMapping("/learning/course")
public class CourseLearningController implements CourseLearningControllerApi {

    @Autowired
    LearningService learningService;

    /**
     * 获取课程学习地址
     * @param courseId
     * @param teachplanId
     * @return
     */
    @Override
    @GetMapping("/getmedia/{courseId}/{teachplanId}")
    public GetMediaResult getmedia(@PathVariable("courseId") String courseId,
                                   @PathVariable("teachplanId")String teachplanId) {

        return learningService.getmedia(courseId,teachplanId);
    }
}