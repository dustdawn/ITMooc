package com.njit.cms.controller;

import com.njit.cms.service.CmsConfigService;
import com.njit.framework.domain.cms.CmsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dustdawn
 * @date 2020/4/27 16:49
 */
@RestController
@RequestMapping("/cms/config")
public class CmsConfigController {
    @Autowired
    private CmsConfigService cmsConfigService;
    @GetMapping("/getModel/{id}")
    public CmsConfig getModel(@PathVariable("id") String id) {
        return cmsConfigService.getModel(id);
    }
}
