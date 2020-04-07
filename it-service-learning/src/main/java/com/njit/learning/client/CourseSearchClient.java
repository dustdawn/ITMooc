package com.njit.learning.client;

import com.njit.framework.client.MoocServiceList;
import com.njit.framework.domain.course.TeachplanMediaPub;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author dustdawn
 * @date 2020/4/4 12:20
 */
@FeignClient(value= MoocServiceList.MOOC_SERVICE_SEARCH)
public interface CourseSearchClient {
    /**
     * 根据课程计划id查询课程媒资
     * @param teachplanId
     * @return
     */
    @GetMapping("/search/course/getmedia/{teachplanId}")
    public TeachplanMediaPub getmedia(@PathVariable("teachplanId") String teachplanId);
}
