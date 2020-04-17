package com.njit.framework.domain.ucenter.ext;

import com.njit.framework.domain.ucenter.MoocRole;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author dustdawn
 * @date 2020/4/13 18:25
 */
@Data
@ToString
public class MoocRoleAuth extends MoocRole {

    List<MoocMenuNode> children;

}
