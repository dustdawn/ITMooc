package com.njit.framework.domain.learning;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author dustdawn
 * @date 2020/4/4 12:04
 */
@Data
@ToString
@Entity
@Table(name="mooc_learning_course")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class LearningCourse implements Serializable {
    private static final long serialVersionUID = -916357210051789799L;
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;
    @Column(name = "course_id")
    private String courseId;
    @Column(name = "user_id")
    private String userId;
}
