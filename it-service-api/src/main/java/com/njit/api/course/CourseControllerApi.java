package com.njit.api.course;

import com.njit.framework.domain.course.CourseBase;
import com.njit.framework.domain.course.CoursePic;
import com.njit.framework.domain.course.Teachplan;
import com.njit.framework.domain.course.TeachplanMedia;
import com.njit.framework.domain.course.ext.CategoryNode;
import com.njit.framework.domain.course.ext.CourseInfo;
import com.njit.framework.domain.course.ext.CourseView;
import com.njit.framework.domain.course.ext.TeachplanNode;
import com.njit.framework.domain.course.request.CourseListRequest;
import com.njit.framework.domain.course.response.CoursePublishResult;
import com.njit.framework.model.response.QueryResponseResult;
import com.njit.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author dustdawn
 * @date 2019/12/8 22:43
 */
@Api(value="课程管理接口",description = "课程管理接口，提供课程的管理、查询接口")
public interface CourseControllerApi {

    //=============================课程信息相关

    /**
     * 查询课程列表
     * @param page
     * @param size
     * @param courseListRequest 查询条件
     * @return
     */
    @ApiOperation("查询课程列表")
    public QueryResponseResult<CourseInfo> findCourseList(int page, int size, CourseListRequest courseListRequest);

    /**
     * 添加课程
     * @param courseBase
     * @return
     */
    @ApiOperation("添加课程")
    public ResponseResult addCourseBase(CourseBase courseBase)  throws RuntimeException;

    /**
     * 根据获取课程信息
     * @param id
     * @return
     */
    @ApiOperation("查看课程信息")
    public CourseBase getCourseBaseById(String id);

    /**
     * 更新课程
     * @param id
     * @param courseBase
     * @return
     */
    @ApiOperation("更新课程基础信息")
    public ResponseResult updateCourseBase(String id, CourseBase courseBase);



    //=============================课程计划相关
    /**
     * 查询课程计划
     * @param courseId
     * @return
     */
    @ApiOperation("课程计划查询")
    public TeachplanNode findTeachplanList(String courseId);

    /**
     * 添加课程计划
     * @param teachplan
     * @return
     */
    @ApiOperation("添加课程计划")
    public ResponseResult addTeachplan(Teachplan teachplan);


    //=============================课程分类相关
    /**
     * 查询分类
     * @return
     */
    @ApiOperation("课程分类集合")
    public CategoryNode findList();



    //=============================课程图片相关
    /**
     * 添加课程图片
     * @param courseId
     * @param picId
     * @return
     */
    @ApiOperation("添加课程图片")
    public ResponseResult addCoursePic(String courseId, String picId);

    /**
     * 查看课程图片
     * @param courseId
     * @return
     */
    @ApiOperation("查看课程图片")
    public CoursePic findCoursePic(String courseId);

    /**
     * 删除课程图片
     * @param courseId
     * @return
     */
    @ApiOperation("删除课程图片")
    public ResponseResult deleteCoursePic(String courseId);


    //=============================课程视图相关
    /**
     * 课程视图查询
     * @param id
     * @return
     */
    @ApiOperation("课程视图查询")
    public CourseView courseView(String id);

    /**
     * 课程预览
     * @param id
     * @return
     */
    @ApiOperation("课程预览")
    public CoursePublishResult preview(String id);


    //=============================课程发布相关
    /**
     * 课程发布
     * @param id
     * @return
     */
    @ApiOperation("课程发布")
    public CoursePublishResult publish(String id);

    /**
     * 保存课程计划与媒资文件关联
     * @param teachplanMedia
     * @return
     */
    @ApiOperation("保存课程计划与媒资文件关联")
    public ResponseResult savemedia(TeachplanMedia teachplanMedia);
}
