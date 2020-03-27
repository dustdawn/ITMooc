package com.njit.course.controller;

import com.njit.api.course.CourseControllerApi;
import com.njit.course.service.*;
import com.njit.framework.domain.course.*;
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
    private CategoryService categoryService;
    @Autowired
    private CourseBaseService courseBaseService;
    @Autowired
    private CoursePicService coursePicService;
    @Autowired
    private CoursePubService coursePubService;
    @Autowired
    private TeachplanMediaService teachplanMediaService;
    @Autowired
    private TeachplanService teachplanService;

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
        return courseBaseService.findCourseList("", page, size, courseListRequest);
    }

    /**
     * 课程添加
     * @param courseBase
     * @return
     */
    @Override
    @PostMapping("/coursebase/add")
    public ResponseResult addCourseBase(@RequestBody CourseBase courseBase) {
        return courseBaseService.addCourseBase(courseBase);
    }

    /**
     * 根据id查询课程
     * @param id
     * @return
     */
    @Override
    @GetMapping("/coursebase/get/{courseId}")
    public CourseBase getCourseBaseById(@PathVariable("courseId") String id) {
        return courseBaseService.getCourseBaseById(id);
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
        return courseBaseService.updateCourseBase(id, courseBase);
    }

    /**
     * 根据课程id查询教学计划
     * @param courseId
     * @return
     */
    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        return teachplanService.findTeachplanList(courseId);
    }

    /**
     * 课程计划添加
     * @param teachplan
     * @return
     */
    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        return teachplanService.addTeachplan(teachplan);
    }

    @Override
    @GetMapping("/teachplan/get/{id}")
    public Teachplan getTeachplanById(@PathVariable("id") String id) {
        return teachplanService.getTeachplanById(id);
    }

    /**
     * 课程计划添加
     * @param teachplan
     * @return
     */
    @Override
    @PutMapping("/teachplan/update/{id}")
    public ResponseResult updateTeachplan(@PathVariable("id") String id, @RequestBody Teachplan teachplan) {
        return teachplanService.updateTeachplan(id, teachplan);
    }

    @Override
    @DeleteMapping("/teachplan/del/{id}")
    public ResponseResult deleteTeachplan(@PathVariable("id") String id) {
        return teachplanService.deleteTeachplan(id);
    }

    /**
     * 课程分类查询
     * @return
     */
    @Override
    @GetMapping("/category/list")
    public CategoryNode findCategoryList() {
        return categoryService.findCategoryList();
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
        return coursePicService.addCoursePic(courseId, pic);
    }

    /**
     * 根据课程id查询课程图片
     * @param courseId
     * @return
     */
    @Override
    @GetMapping("/coursepic/list/{courseId}")
    public CoursePic findCoursePic(@PathVariable("courseId") String courseId) {
        return coursePicService.listCoursePic(courseId);
    }

    /**
     * 删除课程图片
     * @param courseId
     * @return
     */
    @Override
    @DeleteMapping("/coursepic/delete")
    public ResponseResult deleteCoursePic(@RequestParam("courseId") String courseId) {
        return coursePicService.deleteCoursePic(courseId);
    }

    /**
     * 获取课程相关信息
     * @param id
     * @return
     */
    @Override
    @GetMapping("/courseview/{id}")
    public CourseView courseView(@PathVariable("id") String id) {
        return courseBaseService.getCourseView(id);
    }

    /**
     * 课程预览
     * @param id
     * @return
     */
    @Override
    @PostMapping("/preview/{id}")
    public CoursePublishResult preview(@PathVariable("id") String id) {
        return coursePubService.preview(id);
    }

    /**
     * 课程发布
     * @param id
     * @return
     */
    @Override
    @PostMapping("/publish/{id}")
    public CoursePublishResult publish(@PathVariable("id") String id) {
        return coursePubService.publish(id);
    }

    /**
     * 课程媒资绑定
     * @param teachplanMedia
     * @return
     */
    @Override
    @PostMapping("/savemedia")
    public ResponseResult savemedia(@RequestBody TeachplanMedia teachplanMedia) {
        return teachplanMediaService.savemedia(teachplanMedia);
    }
}
