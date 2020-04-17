package com.njit.framework.domain.ucenter.ext;

import com.njit.framework.domain.ucenter.MoocMenu;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 菜单结点
 * @author dustdawn
 * @date 2020/4/13 18:18
 */
@Data
@ToString
public class MoocMenuNode extends MoocMenu {
    List<MoocMenu> children;
}
