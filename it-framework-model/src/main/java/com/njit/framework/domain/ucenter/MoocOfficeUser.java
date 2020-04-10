package com.njit.framework.domain.ucenter;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author dustdawn
 * @date 2020/4/10 14:49
 */
@Data
@ToString
@Entity
@Table(name="mooc_office_user")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class MoocOfficeUser implements Serializable {
    private static final long serialVersionUID = -916357110051689786L;
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;
    @Column(name="office_id")
    private String companyId;
    @Column(name="user_id")
    private String userId;


}
