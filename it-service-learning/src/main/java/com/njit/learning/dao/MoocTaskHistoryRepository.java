package com.njit.learning.dao;

import com.njit.framework.domain.task.MoocTaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dustdawn
 * @date 2020/4/19 16:02
 */
public interface MoocTaskHistoryRepository extends JpaRepository<MoocTaskHistory, String> {

}
