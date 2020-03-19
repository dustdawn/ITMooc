package com.njit.cms.dao;

import com.njit.framework.domain.cms.CmsPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dustdawn
 * @date 2019/11/12 16:09
 */
@Repository
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {
    //泛型指定模型类和主键类型

    /**
     * 根据页面名称查询
     * @param pageName
     * @return
     */
    CmsPage findByPageName(String pageName);

    /**
     * 根据页面名称和类型查询
     * @param pageName
     * @param pageType
     * @return
     */
    CmsPage findByPageNameAndPageType(String pageName, String pageType);

    /**
     * 根据站点和页面类型查询记录数
     * @param siteId
     * @param pageType
     * @return
     */
    int countBySiteIdAndPageType(String siteId, String pageType);

    /**
     * 根据站点和页面类型分页查询
     * @param siteId
     * @param pageType
     * @param pageable
     * @return
     */
    Page<CmsPage> findBySiteIdAndPageType(String siteId, String pageType, Pageable pageable);

    /**
     * 根据唯一索引查询
     * @param siteId
     * @param PageName
     * @param PageWebPath
     * @return
     */
    CmsPage findBySiteIdAndPageNameAndPageWebPath(String siteId, String PageName, String PageWebPath);
}

