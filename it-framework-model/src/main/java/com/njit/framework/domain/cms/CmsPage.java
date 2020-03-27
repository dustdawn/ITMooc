package com.njit.framework.domain.cms;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author dustdawn
 * @date 2020/3/15 23:18
 */
@Data
@ToString
@Document(collection = "cms_page")
public class CmsPage {
    /**
     * 页面名称、别名、访问地址、类型（静态/动态）、页面模版、状态
     */

    //页面ID
    @Id
    private String pageId;
    //别名
    private String pageAliase;

    //站点ID
    private String siteId;
    //页面名称
    private String pageName;
    //访问地址
    private String pageWebPath;

    //模版id
    private String templateId;
    //数据Url
    private String dataUrl;
    //物理路径
    private String pagePhysicalPath;




    //页面模版
    private String pageTemplate;
    //页面静态化内容
    private String pageHtml;
    //状态
    private String pageStatus;
    //创建时间
    private Date pageCreateTime;


    //模版文件Id
    private String templateFileId;
    //静态文件Id
    private String htmlFileId;


}
