package com.njit.course.service;

import com.njit.course.dao.TeachplanMediaPubRepository;
import com.njit.course.dao.TeachplanMediaRepository;
import com.njit.course.dao.TeachplanRepository;
import com.njit.framework.domain.course.Teachplan;
import com.njit.framework.domain.course.TeachplanMedia;
import com.njit.framework.domain.course.TeachplanMediaPub;
import com.njit.framework.domain.course.response.CourseCode;
import com.njit.framework.exception.CustomException;
import com.njit.framework.exception.ExceptionCast;
import com.njit.framework.model.response.CommonCode;
import com.njit.framework.model.response.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author dustdawn
 * @date 2020/3/26 21:20
 */
@Service
public class TeachplanMediaService {
    @Autowired
    private TeachplanRepository teachplanRepository;
    @Autowired
    private TeachplanMediaRepository teachplanMediaRepository;
    @Autowired
    private TeachplanMediaPubRepository teachplanMediaPubRepository;
    /**
     * 保存课程计划与媒资文件的关联信息
     * @param teachplanMedia
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public ResponseResult savemedia(TeachplanMedia teachplanMedia) {
        if (teachplanMedia == null || StringUtils.isEmpty(teachplanMedia.getTeachplanId())) {
            ExceptionCast.cast(CommonCode.FAIL);
        }

        String teachplanId = teachplanMedia.getTeachplanId();
        Optional<Teachplan> teachplanOptional = teachplanRepository.findById(teachplanId);
        if (!teachplanOptional.isPresent()) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //查询课程计划
        Teachplan teachplan = teachplanOptional.get();
        //校验课程计划是否为3级
        String grade = teachplan.getGrade();
        if (StringUtils.isEmpty(grade) || !grade.equals("3")) {
            ExceptionCast.cast(CourseCode.COURSE_MEDIS_GRADEEROR);
        }

        //查询teachplanMedia
        Optional<TeachplanMedia> optionalTeachplanMedia = teachplanMediaRepository.findById(teachplanId);
        TeachplanMedia one = optionalTeachplanMedia.orElse(new TeachplanMedia());

        //将one保存到数据库
        //课程id
        one.setCourseId(teachplan.getCourseid());
        //视频id
        one.setMediaId(teachplanMedia.getMediaId());
        one.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName());
        one.setMediaUrl(teachplanMedia.getMediaUrl());
        one.setTeachplanId(teachplanId);
        teachplanMediaRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 向teachplanMediaPub中保存课程媒资信息
     * @param courseId
     */
    public void saveTeachplanMediaPub(String courseId) {
        //先删除TeachPlanMedia中数据
        teachplanMediaPubRepository.deleteByCourseId(courseId);

        //从TeachPlanMedia中查询
        List<TeachplanMedia> teachplanMediaList = teachplanMediaRepository.findByCourseId(courseId);
        List<TeachplanMediaPub> teachplanMediaPubs = new ArrayList<>();
        //将数据存放到pub中
        for (TeachplanMedia teachplanMedia : teachplanMediaList) {
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            BeanUtils.copyProperties(teachplanMedia ,teachplanMediaPub);
            //添加时间戳
            teachplanMediaPub.setTimestamp(new Date());
            teachplanMediaPubs.add(teachplanMediaPub);
        }

        //将课程媒资信息插入到插入到TeachPlanMedia
        teachplanMediaPubRepository.saveAll(teachplanMediaPubs);

    }
}
