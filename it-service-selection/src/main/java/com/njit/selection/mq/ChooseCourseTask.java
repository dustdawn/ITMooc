package com.njit.selection.mq;

import com.njit.framework.client.RabbitMQList;
import com.njit.framework.domain.task.MoocTask;
import com.njit.selection.service.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 选课mq业务
 * @author dustdawn
 * @date 2020/4/19 15:34
 */
@Component
public class ChooseCourseTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChooseCourseTask.class);

    @Autowired
    TaskService taskService;

    /**
     * Rabbit监听消息队列中完成选课信息
     * @param task
     */
    @RabbitListener(queues = RabbitMQList.QUEUE_LEARNING_FINISHADDCHOOSECOURSE)
    public void receiveFinishChooseCourseTask(MoocTask task){
        if(task != null && StringUtils.isNotEmpty(task.getId())){
            taskService.finishTask(task.getId());
        }
    }

    /**
     * 定时发送添加选课任务
     * 每隔3秒去执行
     */
    @Scheduled(cron="0/3 * * * * *")
    public void sendChoosecourseTask(){
        //得到1分钟之前的时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - 1);
        Date time = calendar.getTime();
        // 查询前100条任务
        List<MoocTask> taskList = taskService.findTaskList(time, 100);
        // 调用service发布消息，将添加选课的任务发送给mq
        for(MoocTask task : taskList){
            //取任务，乐观锁机制，结果大于0取到任务
            if(taskService.getTask(task.getId(), task.getVersion()) > 0){
                //要发送的交换机
                String ex = task.getMqExchange();
                //发送消息要带routingKey
                String routingKey = task.getMqRoutingkey();
                taskService.publish(task, ex, routingKey);
            }
        }
    }
}
