package com.njit.selection.service;

import com.njit.framework.domain.task.MoocTask;
import com.njit.framework.model.response.CommonCode;
import com.njit.framework.model.response.ResponseResult;
import com.njit.selection.dao.MoocTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author dustdawn
 * @date 2020/4/23 14:41
 */
@Service
public class ChooseCourseService {

    @Autowired
    MoocTaskRepository moocTaskRepository;
    /**
     * 选课
     * @param userId
     * @param courseId
     * @return
     */
    public ResponseResult chooseCourse(String userId, String courseId) {

        MoocTask moocTask = new MoocTask();
        moocTask.setCreateTime(new Date());
        moocTask.setMqExchange("exchanges_learning_addchoosecourse");
        moocTask.setMqRoutingkey("addchoosecourse");
        moocTask.setVersion(1);
        String requestBody = "{'userId':'"+userId+"','courseId':'"+courseId+"'}";
        moocTask.setRequestBody(requestBody);
        moocTask.setUpdateTime(new Date());

        moocTaskRepository.save(moocTask);
        return new ResponseResult(CommonCode.SUCCESS);
    }


}
