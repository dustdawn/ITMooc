package com.njit.learning.controller;

import com.njit.api.learning.CourseLearningControllerApi;
import com.njit.framework.domain.learning.LearningCourse;
import com.njit.framework.domain.learning.request.LearningCourceRequest;
import com.njit.framework.domain.learning.response.GetMediaResult;
import com.njit.framework.model.response.QueryResponseResult;
import com.njit.framework.model.response.ResponseResult;
import com.njit.learning.service.LearningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult<LearningCourse> findList(@PathVariable("page") int page,
                                                        @PathVariable("size") int size,
                                                        LearningCourceRequest learningCourceRequest) {
        return learningService.findList(page, size, learningCourceRequest);
    }

    /**
     * 查询是否选课
     * @param userId
     * @param courseId
     * @return
     */
    @GetMapping("/get/status/{userId}/{courseId}")
    public ResponseResult findCourseStatus(@PathVariable("userId") String userId, @PathVariable("courseId") String courseId) {
        return learningService.findCourseStatus(userId, courseId);
    }
}
