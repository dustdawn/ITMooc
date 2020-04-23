package com.njit.framework.domain.course;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author dustdawn
 * @date 2020/4/23 17:13
 */
@Data
@ToString
@Entity
@Table(name="course_teacher")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class CourseTeacher implements Serializable {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;
    private String name;
    @Column(name="user_id")
    private String userId;
    @Column(name="office_id")
    private String officeId;
    private String intro;
}
