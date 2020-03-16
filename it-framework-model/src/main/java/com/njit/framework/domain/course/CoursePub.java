package com.njit.framework.domain.course;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 课程发布表，整合导入ElasticSearch索引
 * @author dustdawn
 * @date 2020/3/16 18:17
 */
@Data
@ToString
@Entity
@Table(name="course_pub")
@GenericGenerator(name = "jpa-assigned", strategy = "assigned")
public class CoursePub implements Serializable {
    private static final long serialVersionUID = -916357110051689487L;
    @Id
    @GeneratedValue(generator = "jpa-assigned")
    @Column(length = 32)
    private String id;
    private String name;
    private String users;
    private String mt;
    private String st;
    private String grade;
    private String description;
    private String pic;//图片
    private Date timestamp;//时间戳
    private String teachplan;//课程计划
    @Column(name="pub_time")
    private String pubTime;//课程发布时间
}
