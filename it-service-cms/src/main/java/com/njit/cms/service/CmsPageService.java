package com.njit.cms.service;

import com.njit.cms.dao.CmsPageRepository;
import com.njit.framework.domain.cms.CmsPage;
import com.njit.framework.domain.cms.request.QueryPageRequest;
import com.njit.framework.domain.cms.response.CmsCode;
import com.njit.framework.domain.cms.response.CmsPageResult;
import com.njit.framework.exception.ExceptionCast;
import com.njit.framework.model.response.CommonCode;
import com.njit.framework.model.response.QueryResponseResult;
import com.njit.framework.model.response.QueryResult;
import com.njit.framework.model.response.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author dustdawn
 * @date 2020/3/22 22:08
 */
@Service
public class CmsPageService {

    @Autowired
    CmsPageRepository cmsPageRepository;

    /**
     * 页面查询方法
     * @param page 页码，从1开始计数
     * @param size 每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {

        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }

        //自定义条件查询
        CmsPage cmsPage = new CmsPage();
        //定义条件匹配器，模糊查询
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());

        //设置条件值
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //设置模板id查询
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //设置页面别名为查询条件
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }

        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);

        //分页参数
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);

        QueryResult queryResult = new QueryResult();
        //数据总记录数
        queryResult.setTotal(all.getTotalElements());
        //数据列表
        queryResult.setList(all.getContent());
        //返回结果集
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    /**
     * 页面添加
     * @param cmsPage
     * @return
     */
    public CmsPageResult add(CmsPage cmsPage) {
        if (null == cmsPage) {
            //抛出非法参数异常，指定异常信息的内容
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //根据唯一索引校验
        CmsPage cmsPageIndex = cmsPageRepository.findBySiteIdAndPageNameAndPageWebPath(cmsPage.getSiteId(), cmsPage.getPageName(), cmsPage.getPageWebPath());

        if (null != cmsPageIndex) {
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        cmsPage.setPageId(null);
        CmsPage save = cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS, save);

    }

    /**
     * 根据页面id获取页面信息
     * @param id
     * @return
     */
    public CmsPage getById(String id) {
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        return optional.orElse(null);
    }

    /**
     * 修改页面信息
     * @param id
     * @param cmsPage
     * @return
     */
    public CmsPageResult update(String id, CmsPage cmsPage) {
        CmsPage byId = this.getById(id);
        if (byId != null) {
            //更新模板id
            byId.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            byId.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            byId.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            byId.setPageName(cmsPage.getPageName());
            //更新访问路径
            byId.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            byId.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            //更新dataUrl
            byId.setDataUrl(cmsPage.getDataUrl());

            cmsPageRepository.save(byId);
            return new CmsPageResult(CommonCode.SUCCESS, byId);
        }
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    /**
     * 删除页面
     * @param id
     * @return
     */
    public ResponseResult delete(String id) {
        CmsPage byId = this.getById(id);
        if (byId == null) {
            return new ResponseResult(CommonCode.FAIL);
        }
        cmsPageRepository.delete(byId);
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
