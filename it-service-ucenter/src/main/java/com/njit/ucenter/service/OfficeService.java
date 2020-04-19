package com.njit.ucenter.service;

import com.njit.framework.domain.ucenter.MoocOffice;
import com.njit.ucenter.dao.MoocOfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dustdawn
 * @date 2020/4/19 13:54
 */
@Service
public class OfficeService {
    @Autowired
    private MoocOfficeRepository moocOfficeRepository;

    public List<MoocOffice> findOfficeList() {
        return moocOfficeRepository.findAll();
    }

    public MoocOffice getOfficeById(String id) {
        return moocOfficeRepository.findById(id).orElse(null);
    }
}
