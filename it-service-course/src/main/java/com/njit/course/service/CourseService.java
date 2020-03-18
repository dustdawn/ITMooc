package com.njit.course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.njit.course.dao.*;
import com.njit.framework.domain.course.CourseBase;
import com.njit.framework.domain.course.CoursePic;
import com.njit.framework.domain.course.Teachplan;
import com.njit.framework.domain.course.ext.CategoryNode;
import com.njit.framework.domain.course.ext.CourseInfo;
import com.njit.framework.domain.course.ext.TeachplanNode;
import com.njit.framework.domain.course.request.CourseListRequest;
import com.njit.framework.exception.CustomException;
import com.njit.framework.exception.ExceptionCast;
import com.njit.framework.model.response.CommonCode;
import com.njit.framework.model.response.QueryResponseResult;
import com.njit.framework.model.response.QueryResult;
import com.njit.framework.model.response.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author dustdawn
 * @date 2020/3/16 23:47
 */
@Service
public class CourseService {
    @Autowired(required = false)
    CourseMapper courseMapper;
    @Autowired
    CourseBaseRepository courseBaseRepository;

    @Autowired(required = false)
    CategoryMapper categoryMapper;

    @Autowired
    CoursePicRepository coursePicRepository;

    @Autowired
    TeachplanRepository teachplanRepository;
    @Autowired(required = false)
    TeachplanMapper teachplanMapper;
    /**
     * 查询我的课程
     * @param office_id
     * @param page
     * @param size
     * @param courseListRequest
     * @return
     */
    public QueryResponseResult<CourseInfo> findCourseList(String office_id, int page, int size, CourseListRequest courseListRequest) {

        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        //courseListRequest.setOfficeId(office_id);

        PageHelper.startPage(page, size);

        Page<CourseInfo> courseList = courseMapper.findCourseListPage(courseListRequest);
        List<CourseInfo> result = courseList.getResult();
        long total = courseList.getTotal();

        QueryResult<CourseInfo> queryResult = new QueryResult<>();
        queryResult.setList(result);
        queryResult.setTotal(total);

        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }

    /**
     * 添加课程
     * @param courseBase
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public ResponseResult addCourseBase(CourseBase courseBase) {
        if (courseBase == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        courseBaseRepository.save(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 获取课程信息
     * @param id
     * @return
     */
    public CourseBase getCourseBaseById(String id) {
        Optional<CourseBase> optional = courseBaseRepository.findById(id);
        return optional.orElse(null);
    }

    /**
     * 课程修改
     * @param id
     * @param courseBase
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public ResponseResult updateCourseBase(String id, CourseBase courseBase) {
        if (StringUtils.isEmpty(id) || courseBase == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        CourseBase courseBaseById = this.getCourseBaseById(id);
        if (courseBaseById != null) {
            BeanUtils.copyProperties(courseBase, courseBaseById);
            courseBaseRepository.save(courseBaseById);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    /**
     * 查询分类列表
     * @return
     */
    public CategoryNode findCategoryList() {
        return categoryMapper.findList();
    }

    /**
     * 保存课程图片
     * 向课程管理数据库添加课程与图片的关联关系
     * @param courseId
     * @param pic
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addCoursePic(String courseId, String pic) {

        CoursePic coursePic = null;
        //查询数据库中已有记录
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        coursePic = optional.orElse(new CoursePic());

        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 根据课程id查询图片信息
     * @param courseId
     * @return
     */
    public CoursePic listCoursePic(String courseId) {
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        return optional.orElse(null);
    }

    /**
     * 删除课程
     * @param courseId
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public ResponseResult deleteCoursePic(String courseId) {
        long l = coursePicRepository.deleteByCourseid(courseId);
        if (l > 0) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);

    }

    /**
     * 添加课程计划
     * @param teachplan
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public ResponseResult addTeachplan(Teachplan teachplan) {
        if (teachplan == null || StringUtils.isEmpty(teachplan.getCourseid()) || StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //取出课程id
        String courseid = teachplan.getCourseid();
        //取出父结点id
        String parentid = teachplan.getParentid();
        //如果父结点为空则获取根节点(根节点为所属课程)，表示添加的为二级节点（第一章）
        if (StringUtils.isEmpty(parentid)) {
            parentid = this.getTeachplanRoot(courseid);
        }


        Teachplan teachplanAdd = new Teachplan();
        //将页面提交的teachplan信息拷贝到teachplanAdd中
        BeanUtils.copyProperties(teachplan, teachplanAdd);
        teachplanAdd.setParentid(parentid);
        teachplanAdd.setCourseid(courseid);

        //通过父结点的目录级别设置当前添加结点的级别
        //此时parentid为父结点的id，取出父结点信息
        Optional<Teachplan> optional = teachplanRepository.findById(parentid);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Teachplan teachplanParent = optional.get();
        teachplanAdd.setDescription(teachplanParent.getDescription() + " " + teachplan.getPname());
        String grade = teachplanParent.getGrade();
        if (grade.equals("1")) {
            teachplanAdd.setGrade("2");
        }else {
            teachplanAdd.setGrade("3");
        }

        //添加当前课程计划
        teachplanRepository.save(teachplanAdd);

        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 查询课程的根节点，如果查询不到自动添加根节点
     * @param courseid
     * @return
     */
    private String getTeachplanRoot(String courseid) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseid);
        if (!optional.isPresent()) {
            return null;
        }

        //查询根节点
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseid, "0");
        if (teachplanList == null || teachplanList.size() <= 0) {
            //查询不到，自动添加根节点（添加的课程计划没有一级目录）
            Teachplan teachplan = new Teachplan();
            teachplan.setParentid("0");
            teachplan.setCourseid(courseid);
            teachplan.setPname(optional.get().getName());
            //设置为一级目录
            teachplan.setGrade("1");
            teachplan.setStatus("0");
            teachplan.setDescription(optional.get().getName());
            teachplanRepository.save(teachplan);
            return teachplan.getId();
        }
        return teachplanList.get(0).getId();
    }

    public TeachplanNode findTeachplanList(String courseId) {
        return teachplanMapper.findList(courseId);
    }
}
