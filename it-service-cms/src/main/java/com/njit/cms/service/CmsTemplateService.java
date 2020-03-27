package com.njit.cms.service;

import com.njit.cms.dao.CmsTemplateRepository;
import com.njit.framework.domain.cms.CmsTemplate;
import com.njit.framework.model.response.CommonCode;
import com.njit.framework.model.response.QueryResponseResult;
import com.njit.framework.model.response.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author dustdawn
 * @date 2020/3/27 21:12
 */
@Service
public class CmsTemplateService {
    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    /**
     * 根据模板id获取模板名称
     * @param id
     * @return
     */
    public String getPageTemplateById(String id) {
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get().getTemplateName();
        }
        return null;
    }

    /**
     * 获取模板列表
     * @return
     */
    public QueryResponseResult findList() {
        List<CmsTemplate> all = cmsTemplateRepository.findAll();
        QueryResult queryResult = new QueryResult();
        //数据总记录数
        queryResult.setTotal(all.size());
        //数据列表
        queryResult.setList(all);
        //返回结果集
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }
}
