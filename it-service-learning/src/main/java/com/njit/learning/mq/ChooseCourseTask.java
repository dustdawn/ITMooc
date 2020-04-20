package com.njit.learning.mq;

import com.alibaba.fastjson.JSON;
import com.njit.framework.client.RabbitMQList;
import com.njit.framework.domain.task.MoocTask;
import com.njit.framework.model.response.ResponseResult;
import com.njit.learning.service.LearningService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author dustdawn
 * @date 2020/4/19 16:18
 */
@Component
public class ChooseCourseTask {

    @Autowired
    private LearningService learningService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 完成添加选课路由key
     */
    public static final String ROUTINGKEY_LEARNING_FINISHADDCHOOSECOURSE = "finishaddchoosecourse";

    /**
     * 监听添加选课任务
     * @param task
     */
    @RabbitListener(queues = RabbitMQList.QUEUE_LEARNING_ADDCHOOSECOURSE)
    public void receiveChoosecourseTask(MoocTask task){

        // 取出消息的内容
        String requestBody = task.getRequestBody();
        Map map = JSON.parseObject(requestBody, Map.class);
        String userId = (String) map.get("userId");
        String courseId = (String) map.get("courseId");

        // 添加选课
        ResponseResult responseResult = learningService.addcourse(userId, courseId, task);
        //选课完成或选课已存在都发送选课完成消息
        if(responseResult.isSuccess()){
            // 添加选课成功，要向mq发送完成添加选课的消息
            rabbitTemplate.convertAndSend(RabbitMQList.EXCHANGES_LEARNING_ADDCHOOSECOURSE, ROUTINGKEY_LEARNING_FINISHADDCHOOSECOURSE, task);
        }
    }
}
