package com.njit.cms.dao;

import com.njit.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dustdawn
 * @date 2019/11/12 16:09
 */
@Repository
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate, String> {

}

