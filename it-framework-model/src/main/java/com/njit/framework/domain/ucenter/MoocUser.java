package com.njit.framework.domain.ucenter;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author dustdawn
 * @date 2020/4/10 14:45
 */
@Data
@ToString
@Entity
@Table(name="mooc_user")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class MoocUser {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String mobile;
    private String status;
    @Column(name="create_time")
    private Date createTime;
    @Column(name="update_time")
    private Date updateTime;
    @Column(name="office_id")
    private String officeId;


}