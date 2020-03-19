package com.njit.cms.dao;

import com.njit.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dustdawn
 * @date 2019/11/12 16:09
 */
@Repository
public interface SysDictionaryRepository extends MongoRepository<SysDictionary, String> {
    /**
     * 根据字典表的Type查询返回数据模型对象
     * @param dType
     * @return
     */
    public SysDictionary findByDType(String dType);
}

