package com.njit.api.learning;

import com.njit.framework.domain.learning.LearningCourse;
import com.njit.framework.domain.learning.request.LearningCourceRequest;
import com.njit.framework.domain.learning.response.GetMediaResult;
import com.njit.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author dustdawn
 * @date 2020/3/8 22:07
 */
@Api(value = "录播课程学习管理", description = "录播课程学习管理")
public interface CourseLearningControllerApi {
    /**
     * 获取课程学习地址
     * @param courseId
     * @param teachplanId
     * @return
     */
    @ApiOperation("获取课程学习地址")
    public GetMediaResult getmedia(String courseId, String teachplanId);

    /**
     * 根据用户id查询课程列表
     * @param page
     * @param size
     * @param learningCourseRequest
     * @return
     */
    @ApiOperation("查询课程列表")
    public QueryResponseResult<LearningCourse> findList(int page, int size, LearningCourceRequest learningCourseRequest);
}
