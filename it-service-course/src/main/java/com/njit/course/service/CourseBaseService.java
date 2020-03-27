package com.njit.course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.njit.course.dao.CourseBaseRepository;
import com.njit.course.dao.CourseMapper;
import com.njit.course.dao.CoursePicRepository;
import com.njit.course.dao.TeachplanMapper;
import com.njit.framework.domain.course.CourseBase;
import com.njit.framework.domain.course.CoursePic;
import com.njit.framework.domain.course.ext.CourseInfo;
import com.njit.framework.domain.course.ext.CourseView;
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
 * @date 2020/3/26 21:16
 */
@Service
public class CourseBaseService {
    @Autowired(required = false)
    private CourseMapper courseMapper;
    @Autowired
    private CourseBaseRepository courseBaseRepository;
    @Autowired
    private CoursePicRepository coursePicRepository;
    @Autowired(required = false)
    private TeachplanMapper teachplanMapper;

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
        // 设置课程状态为制作中
        courseBase.setStatus("202001");
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
     * 静态页面查询课程视图，包括基本信息，图片，课程计划等
     * @param id
     * @return
     */
    public CourseView getCourseView(String id) {
        CourseView courseView = new CourseView();
        //查询课程信息
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(id);
        if (courseBaseOptional.isPresent()) {
            CourseBase courseBase = courseBaseOptional.get();
            courseView.setCourseBase(courseBase);
        }
        Optional<CoursePic> coursePicOptional = coursePicRepository.findById(id);
        if (coursePicOptional.isPresent()) {
            CoursePic coursePic = coursePicOptional.get();
            courseView.setCoursePic(coursePic);
        }
        TeachplanNode teachplanNode = teachplanMapper.findList(id);
        courseView.setTeachplanNode(teachplanNode);
        return courseView;
    }
}
