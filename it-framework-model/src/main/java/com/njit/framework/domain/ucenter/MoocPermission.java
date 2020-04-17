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
@Table(name="mooc_permission")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class MoocPermission {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;
    @Column(name="role_id")
    private String roleId;
    @Column(name="menu_id")
    private String menuId;
    @Column(name="create_time")
    private Date createTime;


}