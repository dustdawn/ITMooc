package com.njit.cms.controller;

import com.njit.api.cms.CmsPageControllerApi;
import com.njit.cms.service.CmsPageService;
import com.njit.framework.domain.cms.CmsPage;
import com.njit.framework.domain.cms.request.QueryPageRequest;
import com.njit.framework.domain.cms.response.CmsPageResult;
import com.njit.framework.domain.cms.response.CmsPostPageResult;
import com.njit.framework.model.response.QueryResponseResult;
import com.njit.framework.model.response.ResponseResult;
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
    CmsPageService cmsPageService;

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

    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable("id") String id) {
        return cmsPageService.delete(id);
    }

    @Override
    public ResponseResult post(String id) {
        return null;
    }

    @Override
    public CmsPageResult save(CmsPage cmsPage) {
        return null;
    }

    @Override
    public CmsPostPageResult postPageQuick(CmsPage cmsPage) {
        return null;
    }
}
