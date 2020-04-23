package com.njit.framework.domain.course.request;

import com.njit.framework.model.request.RequestData;
import lombok.Data;
import lombok.ToString;

/**
 * 课程列表搜索的查询条件
 * @author dustdawn
 * @date 2020/3/16 22:42
 */
@Data
@ToString
public class CourseListRequest extends RequestData {
    /**
     * 机构Id
     */
    private String officeId;

    /**
     * 课程ids
     */
    private String courseIds;
}
