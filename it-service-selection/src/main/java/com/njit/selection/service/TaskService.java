package com.njit.selection.service;

import com.njit.framework.domain.task.MoocTask;
import com.njit.framework.domain.task.MoocTaskHistory;
import com.njit.selection.dao.MoocTaskHistoryRepository;
import com.njit.selection.dao.MoocTaskRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author dustdawn
 * @date 2020/4/19 15:17
 */
@Service
public class TaskService {
    @Autowired
    private MoocTaskRepository moocTaskRepository;
    @Autowired
    private MoocTaskHistoryRepository moocTaskHistoryRepository;
    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 查询前n条任务
     * @param updateTime
     * @param size
     * @return
     */
    public List<MoocTask> findTaskList(Date updateTime, int size){
        //设置分页参数
        Pageable pageable = PageRequest.of(0,size);
        //查询前n条任务
        Page<MoocTask> all = moocTaskRepository.findByUpdateTimeBefore(pageable, updateTime);
        List<MoocTask> list = all.getContent();
        return list;
    }

    /**
     * 发布选课任务消息
     * @param task
     * @param ex
     * @param routingKey
     */
    public void publish(MoocTask task,String ex,String routingKey){
        // 根据id查询选课任务
        Optional<MoocTask> optional = moocTaskRepository.findById(task.getId());
        if(optional.isPresent()){
            // 将选课任务信息作为消息队列信息发出
            rabbitTemplate.convertAndSend(ex, routingKey, task);
            // 更新任务时间
            MoocTask one = optional.get();
            one.setUpdateTime(new Date());
            moocTaskRepository.save(one);
        }
    }

    //获取任务
    @Transactional(rollbackFor = Exception.class)
    public int getTask(String id, int version){
        //通过乐观锁的方式来更新数据表，如果结果大于0说明取到任务
        int count = moocTaskRepository.updateTaskVersion(id, version);
        return count;
    }

    //完成任务
    @Transactional(rollbackFor = Exception.class)
    public void finishTask(String taskId){
        Optional<MoocTask> optional = moocTaskRepository.findById(taskId);
        if(optional.isPresent()){
            //当前任务
            MoocTask task = optional.get();
            //历史任务
            MoocTaskHistory taskHistory = new MoocTaskHistory();
            BeanUtils.copyProperties(task,taskHistory);
            moocTaskHistoryRepository.save(taskHistory);
            moocTaskRepository.delete(task);
        }
    }

}
