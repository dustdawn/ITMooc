package com.njit.search.controller;

import com.njit.api.search.EsCourseControllerApi;
import com.njit.framework.domain.course.CoursePub;
import com.njit.framework.domain.course.TeachplanMediaPub;
import com.njit.framework.domain.search.CourseSearchParam;
import com.njit.framework.model.response.QueryResponseResult;
import com.njit.framework.model.response.QueryResult;
import com.njit.search.service.EsCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author dustdawn
 * @date 2020/2/23 15:15
 */
@RestController
@RequestMapping("/search/course")
public class EsCourseController implements EsCourseControllerApi {
    @Autowired
    private EsCourseService esCourseService;

    /**
     * 搜索课程条件
     * @param page
     * @param size
     * @param param 综合查询条件
     * @return
     */
    @Override
    @GetMapping(value = "/list/{page}/{size}")
    public QueryResponseResult<CoursePub> list(@PathVariable("page") int page, @PathVariable("size") int size, CourseSearchParam param) {
        return esCourseService.list(page, size, param);
    }

    /**
     * 根据课程id查询课程信息
     * @param courseId
     * @return
     */
    @Override
    @GetMapping("getall/{id}")
    public Map<String, CoursePub> getall(@PathVariable("id") String courseId) {
        return esCourseService.getall(courseId);
    }


    /**
     * 根据课程计划id查询媒资信息
     * @param teachplanId
     * @return
     */
    @GetMapping("/getmedia/{teachplanId}")
    @Override
    public TeachplanMediaPub getmedia(@PathVariable("teachplanId") String teachplanId) {
        String[] teachplanIds = new String[]{teachplanId};
        QueryResponseResult<TeachplanMediaPub> queryResponseResult = esCourseService.getmedia(teachplanIds);
        QueryResult<TeachplanMediaPub> queryResult = queryResponseResult.getQueryResult();
        if (queryResult != null) {
            List<TeachplanMediaPub> list = queryResult.getList();
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        }
        return new TeachplanMediaPub();
    }
}
