package com.njit.framework.domain.course.ext;

import com.njit.framework.domain.course.CourseBase;
import lombok.Data;
import lombok.ToString;

/**
 * 课程信息扩展课程图片
 * @author dustdawn
 * @date 2020/3/16 22:36
 */
@Data
@ToString
public class CourseInfo extends CourseBase {

    /**
     * 课程图片
     */
    private String pic;


}