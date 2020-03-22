package com.njit.cms.dao;

import com.njit.framework.domain.cms.CmsPage;
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
     * 根据唯一索引查询
     * @param siteId
     * @param PageName
     * @param PageWebPath
     * @return
     */
    CmsPage findBySiteIdAndPageNameAndPageWebPath(String siteId, String PageName, String PageWebPath);
}

