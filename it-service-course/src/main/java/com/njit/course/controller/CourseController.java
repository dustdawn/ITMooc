package com.njit.course.controller;

import com.njit.api.course.CourseControllerApi;
import com.njit.course.service.CourseService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author dustdawn
 * @date 2020/3/16 23:30
 */
@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {
    @Autowired
    CourseService courseService;

    /**
     * 课程信息列表查询
     * @param page
     * @param size
     * @param courseListRequest 查询条件
     * @return
     */
    @Override
    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult<CourseInfo> findCourseList(@PathVariable("page") int page, @PathVariable("size") int size,
                                                          CourseListRequest courseListRequest) {
        return courseService.findCourseList("", page, size, courseListRequest);
    }

    /**
     * 课程添加
     * @param courseBase
     * @return
     */
    @Override
    @PostMapping("/coursebase/add")
    public ResponseResult addCourseBase(@RequestBody CourseBase courseBase) {
        return courseService.addCourseBase(courseBase);
    }

    /**
     * 根据id查询课程
     * @param id
     * @return
     */
    @Override
    @GetMapping("/coursebase/get/{courseId}")
    public CourseBase getCourseBaseById(@PathVariable("courseId") String id) {
        return courseService.getCourseBaseById(id);
    }

    /**
     * 根据id修改课程
     * @param id
     * @param courseBase
     * @return
     */
    @Override
    @PutMapping("/coursebase/update/{id}")
    public ResponseResult updateCourseBase(@PathVariable("id") String id, @RequestBody CourseBase courseBase) {
        return courseService.updateCourseBase(id, courseBase);
    }

    /**
     * 根据课程id查询教学计划
     * @param courseId
     * @return
     */
    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        return courseService.findTeachplanList(courseId);
    }

    /**
     * 课程计划添加
     * @param teachplan
     * @return
     */
    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }

    @Override
    @GetMapping("/teachplan/get/{id}")
    public Teachplan getTeachplanById(@PathVariable("id") String id) {
        return courseService.getTeachplanById(id);
    }

    /**
     * 课程计划添加
     * @param teachplan
     * @return
     */
    @Override
    @PutMapping("/teachplan/update/{id}")
    public ResponseResult updateTeachplan(@PathVariable("id") String id, @RequestBody Teachplan teachplan) {
        return courseService.updateTeachplan(id, teachplan);
    }

    @Override
    @DeleteMapping("/teachplan/del/{id}")
    public ResponseResult deleteTeachplan(@PathVariable("id") String id) {
        return courseService.deleteTeachplan(id);
    }

    /**
     * 课程分类查询
     * @return
     */
    @Override
    @GetMapping("/category/list")
    public CategoryNode findCategoryList() {
        return courseService.findCategoryList();
    }

    /**
     * 课程图片添加
     * @param courseId
     * @param pic
     * @return
     */
    @Override
    @PostMapping("/coursepic/add")
    public ResponseResult addCoursePic(@RequestParam("courseId") String courseId, @RequestParam("pic") String pic) {
        return courseService.addCoursePic(courseId, pic);
    }

    /**
     * 根据课程id查询课程图片
     * @param courseId
     * @return
     */
    @Override
    @GetMapping("/coursepic/list/{courseId}")
    public CoursePic findCoursePic(@PathVariable("courseId") String courseId) {
        return courseService.listCoursePic(courseId);
    }

    /**
     * 删除课程图片
     * @param courseId
     * @return
     */
    @Override
    @DeleteMapping("/coursepic/delete")
    public ResponseResult deleteCoursePic(@RequestParam("courseId") String courseId) {
        return courseService.deleteCoursePic(courseId);
    }

    @Override
    @GetMapping("/courseview/{id}")
    public CourseView courseView(@PathVariable("id") String id) {
        return courseService.getCourseView(id);
    }

    @Override
    public CoursePublishResult preview(String id) {
        return null;
    }

    @Override
    public CoursePublishResult publish(String id) {
        return null;
    }

    @Override
    @PostMapping("/savemedia")
    public ResponseResult savemedia(@RequestBody TeachplanMedia teachplanMedia) {
        return courseService.savemedia(teachplanMedia);
    }
}
