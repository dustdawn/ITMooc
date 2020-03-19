package com.njit.course.dao;

import com.njit.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author dustdawn
 * @date 2019/12/8 22:52
 */
public interface TeachplanRepository extends JpaRepository<Teachplan,String> {
    /**
     * 根据课程id和父结点id查询teachplan
     * SELECT * FROM `teachplan` WHERE parentid = '0' AND courseid = '4028e581617f945f01617f9dabc40000'
     * @param courseid
     * @param parentid
     * @return
     */
    public List<Teachplan> findByCourseidAndParentid(String courseid, String parentid);


}
