package com.njit.cms.service;

import com.njit.cms.dao.CmsConfigRepository;
import com.njit.framework.domain.cms.CmsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dustdawn
 * @date 2020/4/27 16:51
 */
@Service
public class CmsConfigService {
    @Autowired
    private CmsConfigRepository cmsConfigRepository;

    public CmsConfig getModel(String id) {
        return cmsConfigRepository.findById(id).orElse(null);
    }
}
