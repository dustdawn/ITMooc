package com.njit.framework.domain.cms;

import lombok.Data;
import lombok.ToString;

/**
 * @author dustdawn
 * @date 2020/3/15 23:19
 */
@Data
@ToString
public class CmsPageParam {
   //参数名称
    private String pageParamName;
    //参数值
    private String pageParamValue;

}
