package com.njit.cms.controller;

import com.njit.api.cms.CmsTemplateControllerApi;
import com.njit.cms.service.CmsTemplateService;
import com.njit.framework.model.response.QueryResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dustdawn
 * @date 2020/3/27 21:54
 */
@RestController
@RequestMapping("/cms/template")
public class CmsTemplateController implements CmsTemplateControllerApi {
    @Autowired
    private CmsTemplateService cmsTemplateService;

    @Override
    @GetMapping("/list")
    public QueryResponseResult findList() {
        return cmsTemplateService.findList();
    }
}
