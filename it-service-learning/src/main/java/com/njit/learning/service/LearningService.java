package com.njit.learning.service;

import com.njit.framework.domain.course.TeachplanMediaPub;
import com.njit.framework.domain.learning.LearningCode;
import com.njit.framework.domain.learning.LearningCourse;
import com.njit.framework.domain.learning.response.GetMediaResult;
import com.njit.framework.domain.task.MoocTask;
import com.njit.framework.domain.task.MoocTaskHistory;
import com.njit.framework.exception.ExceptionCast;
import com.njit.framework.model.response.CommonCode;
import com.njit.framework.model.response.ResponseResult;
import com.njit.learning.client.CourseSearchClient;
import com.njit.learning.dao.LearningCourseRepository;
import com.njit.learning.dao.MoocTaskHistoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    @Autowired
    private MoocTaskHistoryRepository moocTaskHistoryRepository;
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

    /**
     * 添加选课
     * @param userId
     * @param courseId
     * @param task 选课任务
     * @return
     */
    @Transactional
    public ResponseResult addcourse(String userId,
                                    String courseId,
                                    MoocTask task){
        if (StringUtils.isEmpty(courseId)) {
            ExceptionCast.cast(LearningCode.LEARNING_GETMEDIA_ERROR);
        }

        // 获得选课关系信息
        LearningCourse learningCourse = learningCourseRepository.findByUserIdAndCourseId(userId, courseId);

        //添加新的选课记录
        learningCourse = new LearningCourse();
        learningCourse.setUserId(userId);
        learningCourse.setCourseId(courseId);
        learningCourseRepository.save(learningCourse);

        //向历史任务表查询记录，如不存在添加选课任务
        Optional<MoocTaskHistory> optional = moocTaskHistoryRepository.findById(task.getId());
        if(!optional.isPresent()){
            //添加历史任务
            MoocTaskHistory taskHistory = new MoocTaskHistory();
            BeanUtils.copyProperties(task, taskHistory);
            moocTaskHistoryRepository.save(taskHistory);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
