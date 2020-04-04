package com.njit.api.search;

import com.njit.framework.domain.course.CoursePub;
import com.njit.framework.domain.course.TeachplanMediaPub;
import com.njit.framework.domain.search.CourseSearchParam;
import com.njit.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/**
 * @author dustdawn
 * @date 2020/2/23 15:01
 */
@Api(value = "课程搜索接口",description = "课程搜索", tags = {"课程搜索"})
public interface EsCourseControllerApi {
    /**
     * 搜索课程条件
     * @param page
     * @param size
     * @param param 综合查询条件
     * @return
     */
    @ApiOperation("课程综合搜索")
    public QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam param);

    /**
     * 根据课程id查询课程信息
     * @param courseId
     * @return
     */
    @ApiOperation("根据课程id查询课程信息")
    public Map<String, CoursePub> getall(String courseId);

    /**
     * 根据课程计划id查询媒资信息
     * @param teachplanId
     * @return
     */
    @ApiOperation("根据课程计划id查询媒资信息")
    public TeachplanMediaPub getmedia(String teachplanId);
}
