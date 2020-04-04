package com.njit.framework.domain.search;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 课程搜索参数结构
 * @author dustdawn
 * @date 2020/3/31 22:56
 */
@Data
@ToString
public class CourseSearchParam implements Serializable {
    //关键字
    String keyword;
    //一级分类
    String mt;
    //二级分类
    String st;

    //难度等级
    String grade;


    //排序字段
    String sort;
    //过滤字段
    String filter;

}

