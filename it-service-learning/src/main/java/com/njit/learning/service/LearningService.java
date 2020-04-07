package com.njit.learning.service;

import com.njit.framework.domain.course.TeachplanMediaPub;
import com.njit.framework.domain.learning.LearningCode;
import com.njit.framework.domain.learning.response.GetMediaResult;
import com.njit.framework.exception.ExceptionCast;
import com.njit.framework.model.response.CommonCode;
import com.njit.learning.client.CourseSearchClient;
import com.njit.learning.dao.LearningCourseRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dustdawn
 * @date 2020/4/4 12:18
 */
@Service
public class LearningService {
    @Autowired
    private CourseSearchClient courseSearchClient;
    @Autowired
    private LearningCourseRepository learningCourseRepository;
    /**
     * 获取课程学习地址（视频播放地址）
     * @param courseId
     * @param teachplanId
     * @return
     */
    public GetMediaResult getmedia(String courseId, String teachplanId) {
        //校验学生的学生权限...

        //远程调用搜索服务查询课程计划所对应的课程媒资信息
        TeachplanMediaPub teachplanMediaPub = courseSearchClient.getmedia(teachplanId);
        if(teachplanMediaPub == null || StringUtils.isEmpty(teachplanMediaPub.getMediaUrl())){
            //获取学习地址错误
            ExceptionCast.cast(LearningCode.LEARNING_GETMEDIA_ERROR);
        }
        return new GetMediaResult(CommonCode.SUCCESS,teachplanMediaPub.getMediaUrl());
    }
}
