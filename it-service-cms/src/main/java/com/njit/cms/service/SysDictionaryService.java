package com.njit.cms.service;

import com.njit.cms.dao.SysDictionaryRepository;
import com.njit.framework.domain.system.SysDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dustdawn
 * @date 2020/3/19 19:08
 */
@Service
public class SysDictionaryService {
    @Autowired
    private SysDictionaryRepository sysDictionaryRepository;
    public SysDictionary getByType(String dType) {
        return sysDictionaryRepository.findByDType(dType);
    }
}
