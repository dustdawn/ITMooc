package com.njit.framework.domain.ucenter;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author dustdawn
 * @date 2020/4/10 14:48
 */
@Data
@ToString
@Entity
@Table(name="mooc_user_role")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class MoocUserRole {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name="user_id")
    private String userId;
    @Column(name="role_id")
    private String roleId;
    private String creator;
    @Column(name="create_time")
    private Date createTime;

}
