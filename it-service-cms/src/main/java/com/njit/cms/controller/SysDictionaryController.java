package com.njit.cms.controller;

import com.njit.api.sys.SysDictionaryControllerApi;
import com.njit.cms.service.SysDictionaryService;
import com.njit.framework.domain.system.SysDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dustdawn
 * @date 2020/3/19 19:08
 */
@RestController
@RequestMapping("/sys")
public class SysDictionaryController implements SysDictionaryControllerApi {
    @Autowired
    private SysDictionaryService sysDictionaryService;
    @Override
    @GetMapping("/dictionary/get/{dType}")
    public SysDictionary getByType(@PathVariable("dType") String type) {
        return sysDictionaryService.getByType(type);
    }
}
