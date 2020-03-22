package com.njit.framework.domain.cms.request;

import com.njit.framework.model.request.RequestData;
import lombok.Data;

/**
 * 页面请求结构
 * @author dustdawn
 * @date 2019/11/12 10:01
 */
@Data
public class QueryPageRequest extends RequestData {
    //站点id
    private String siteId;
    //页面ID
    private String pageId;
    // 页面名称
    private String pageName;
    // 别名
    private String pageAliase;
    // 模版id
    private String templateId;

}
