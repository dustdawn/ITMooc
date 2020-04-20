package com.njit.course.client;

import com.njit.framework.client.MoocServiceList;
import com.njit.framework.domain.cms.CmsPage;
import com.njit.framework.domain.cms.response.CmsPageResult;
import com.njit.framework.domain.cms.response.CmsPostPageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author dustdawn
 * @date 2019/12/13 23:13
 * FeignClient指定服务名
 */
@FeignClient(value = MoocServiceList.MOOC_SERVICE_CMS)
public interface CmsPageClient {
    /**
     * 根据页面id查询页面信息，远程调用cms请求数据
     * 用GetMapping标识远程调用的http的方法类型
     * @param id
     * @return
     */
    @GetMapping("/cms/page/get/{id}")
    public CmsPage findCmaPageById(@PathVariable("id") String id);

    /**
     * 添加页面，用于页面预览
     * 返回对象一定要有无参构造方法
     * @param cmsPage
     * @return
     */
    @PostMapping("/cms/page/save")
    public CmsPageResult saveCmsPage(@RequestBody CmsPage cmsPage);

    /**
     * 一键发布
     * @param cmsPage
     * @return
     */
    @PostMapping("/cms/page/postPageQuick")
    public CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage);
}
