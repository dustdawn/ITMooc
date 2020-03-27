package com.njit.course.service;

import com.njit.course.dao.CoursePicRepository;
import com.njit.framework.domain.course.CoursePic;
import com.njit.framework.exception.CustomException;
import com.njit.framework.model.response.CommonCode;
import com.njit.framework.model.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author dustdawn
 * @date 2020/3/26 21:18
 */
@Service
public class CoursePicService {
    @Autowired
    private CoursePicRepository coursePicRepository;

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
     * 删除课程图片
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
}
