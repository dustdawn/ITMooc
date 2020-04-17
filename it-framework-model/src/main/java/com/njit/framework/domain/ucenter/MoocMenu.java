package com.njit.framework.domain.ucenter;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author dustdawn
 * @date 2020/4/10 14:41
 */
@Data
@ToString
@Entity
@Table(name="mooc_menu")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class MoocMenu {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;
    private String code;
    @Column(name="p_id")
    private String pId;
    @Column(name="menu_name")
    private String menuName;
    @Column(name="is_menu")
    private String isMenu;
    private Integer level;
    private Integer sort;
    private String status;

}
