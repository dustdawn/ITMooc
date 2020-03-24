package com.njit.cms.service;

import com.njit.cms.dao.CmsSiteRepository;
import com.njit.framework.domain.cms.CmsSite;
import com.njit.framework.model.response.CommonCode;
import com.njit.framework.model.response.QueryResponseResult;
import com.njit.framework.model.response.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author dustdawn
 * @date 2019/12/2 22:08
 */
@Service
public class CmsSiteService {
    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    /**
     * 查询站点列表
     * @return
     */
    public QueryResponseResult findList() {
        List<CmsSite> all = cmsSiteRepository.findAll();
        QueryResult queryResult = new QueryResult();
        //数据总记录数
        queryResult.setTotal(all.size());
        //数据列表
        queryResult.setList(all);
        //返回结果集
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    /**
     * 根据站点id查询站点信息
     * @return
     */
    public CmsSite findCmsSiteById(String siteId) {
        Optional<CmsSite> optional = cmsSiteRepository.findById(siteId);
        return optional.orElse(null);
    }
}
