package com.njit.framework.domain.course.ext;

import com.njit.framework.domain.course.CourseBase;
import com.njit.framework.domain.course.CoursePic;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author dustdawn
 * @date 2019/12/23 15:20
 */
@Data
@NoArgsConstructor
@ToString
public class CourseView implements Serializable {
    private CourseBase courseBase;
    private CoursePic coursePic;

    private TeachplanNode teachplanNode;
}
