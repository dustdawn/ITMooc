package com.njit.framework.domain.course.ext;

import com.njit.framework.domain.course.Teachplan;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 课程计划 树形结构类型
 * @author dustdawn
 * @date 2020/3/16 22:40
 */
@Data
@ToString
public class TeachplanNode extends Teachplan {

    List<TeachplanNode> children;

    /**
     * 媒资文件id
     */
    String mediaId;
    /**
     * 媒资文件原始名称
     */
    String mediaFileOriginalName;

}
