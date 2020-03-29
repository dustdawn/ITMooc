package com.njit.cms.controller;

import com.njit.api.cms.CmsPageControllerApi;
import com.njit.cms.service.CmsPageService;
import com.njit.framework.domain.cms.CmsPage;
import com.njit.framework.domain.cms.request.QueryPageRequest;
import com.njit.framework.domain.cms.response.CmsCode;
import com.njit.framework.domain.cms.response.CmsPageResult;
import com.njit.framework.domain.cms.response.CmsPostPageResult;
import com.njit.framework.model.response.CommonCode;
import com.njit.framework.model.response.QueryResponseResult;
import com.njit.framework.model.response.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author dustdawn
 * @date 2020/3/22 22:06
 */
@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    private CmsPageService cmsPageService;

    /**
     * 查询页面列表
     * @param page
     * @param size
     * @param queryPageRequest
     * @return
     */
    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page")int page,
                                        @PathVariable("size")int size,
                                        QueryPageRequest queryPageRequest) {
        return cmsPageService.findList(page, size, queryPageRequest);
    }

    /**
     * 添加页面
     * @param cmsPage
     * @return
     */
    @Override
    @PostMapping("/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return cmsPageService.add(cmsPage);
    }

    /**
     * 根据id查询页面
     * @param id
     * @return
     */
    @Override
    @GetMapping("/get/{id}")
    public CmsPage findById(@PathVariable("id") String id) {
        return cmsPageService.getById(id);
    }

    /**
     * 修改页面
     * @param id
     * @param cmsPage
     * @return
     */
    @Override
    @PutMapping("/edit/{id}")
    public CmsPageResult edit(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        return cmsPageService.update(id, cmsPage);
    }

    /**
     * 页面删除
     * @param id
     * @return
     */
    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable("id") String id) {
        return cmsPageService.delete(id);
    }

    /**
     * 页面发布
     * @param id
     * @return
     */
    @Override
    @PostMapping("/postPage/{pageId}")
    public ResponseResult post(@PathVariable("pageId") String id) {
        return cmsPageService.post(id);
    }

    /**
     * 更新或保存页面
     * @param cmsPage
     * @return
     */
    @Override
    @PostMapping("/save")
    public CmsPageResult save(@RequestBody CmsPage cmsPage) {
        return cmsPageService.save(cmsPage);
    }

    /**
     * 一键发布
     * @param cmsPage
     * @return
     */
    @Override
    @PostMapping("/postPageQuick")
    public CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage) {
        return cmsPageService.postPageQuick(cmsPage);
    }

    @PostMapping("/generateHtml/{id}")
    public ResponseResult generateHtml(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        CmsPage byId = cmsPageService.getById(id);
        if (byId == null) {
            return new ResponseResult(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        byId.setPageHtml(cmsPage.getPageHtml());
        cmsPageService.update(id, byId);
        String generate = cmsPageService.getPageHtml(id);
        if (StringUtils.isEmpty(generate)) {
            return new ResponseResult(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }


}
