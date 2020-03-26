package com.njit.cms.controller;

import com.njit.api.cms.CmsSiteControllerApi;
import com.njit.framework.model.response.QueryResponseResult;
import com.njit.cms.service.CmsSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dustdawn
 * @date 2019/12/2 17:54
 */
@RestController
@RequestMapping("cms/site")
public class CmsSiteController implements CmsSiteControllerApi {

    @Autowired
    private CmsSiteService cmsSiteServer;

    @Override
    @GetMapping("/list")
    public QueryResponseResult findList() {
        return cmsSiteServer.findList();
    }
}
