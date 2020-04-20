package com.njit.selection.dao;

import com.njit.framework.domain.task.MoocTaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dustdawn
 * @date 2020/4/19 15:16
 */
public interface MoocTaskHistoryRepository extends JpaRepository<MoocTaskHistory, String> {
}
