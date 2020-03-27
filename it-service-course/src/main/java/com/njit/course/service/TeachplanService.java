package com.njit.course.service;

import com.njit.course.dao.CourseBaseRepository;
import com.njit.course.dao.TeachplanMapper;
import com.njit.course.dao.TeachplanRepository;
import com.njit.framework.domain.course.CourseBase;
import com.njit.framework.domain.course.Teachplan;
import com.njit.framework.domain.course.ext.TeachplanNode;
import com.njit.framework.exception.CustomException;
import com.njit.framework.exception.ExceptionCast;
import com.njit.framework.model.response.CommonCode;
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
 * @date 2020/3/26 21:10
 */
@Service
public class TeachplanService {

    @Autowired(required = false)
    private TeachplanMapper teachplanMapper;
    @Autowired
    private TeachplanRepository teachplanRepository;
    @Autowired
    private CourseBaseRepository courseBaseRepository;

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
            //修改课程状态为202003
            CourseBase courseBase = optional.get();
            courseBase.setStatus("202003");
            courseBaseRepository.save(courseBase);

            return teachplan.getId();
        }
        return teachplanList.get(0).getId();
    }

    public TeachplanNode findTeachplanList(String courseId) {
        return teachplanMapper.findList(courseId);
    }

    //根据id查询teachplan
    public Teachplan getTeachplanById(String id) {
        Optional<Teachplan> optional = teachplanRepository.findById(id);
        return optional.orElse(null);
    }

    /**
     * 删除课程计划
     * @param id
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public ResponseResult deleteTeachplan(String id) {
        if (this.getTeachplanById(id) != null) {
            teachplanRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    /**
     * 修改课程计划
     * @param teachplan
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public ResponseResult updateTeachplan(String id, Teachplan teachplan) {
        if (StringUtils.isEmpty(id) || teachplan == null) {
            return new ResponseResult(CommonCode.INVALID_PARAM);
        }
        Teachplan teachplanById = this.getTeachplanById(id);
        if (teachplanById != null) {
            BeanUtils.copyProperties(teachplan, teachplanById);
            teachplanRepository.save(teachplanById);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);

    }
}
