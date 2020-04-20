package com.njit.selection.dao;

import com.njit.framework.domain.task.MoocTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

/**
 * @author dustdawn
 * @date 2020/4/19 15:15
 */
public interface MoocTaskRepository extends JpaRepository<MoocTask, String> {
    /**
     * 查询某个时间之间的前n条任务
     * @param pageable
     * @param updateTime
     * @return
     */
    Page<MoocTask> findByUpdateTimeBefore(Pageable pageable, Date updateTime);

    @Modifying
    @Query("update MoocTask t set t.version = :version+1 where t.id = :id and t.version = :version")
    public int updateTaskVersion(@Param(value = "id") String id, @Param(value = "version") int version);
}
