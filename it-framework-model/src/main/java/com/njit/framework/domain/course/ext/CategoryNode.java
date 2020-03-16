package com.njit.framework.domain.course.ext;

import com.njit.framework.domain.course.Category;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 课程分类树形结构
 * @author dustdawn
 * @date 2020/3/16 22:56
 */
@Data
@ToString
public class CategoryNode extends Category {

    List<CategoryNode> children;

}
