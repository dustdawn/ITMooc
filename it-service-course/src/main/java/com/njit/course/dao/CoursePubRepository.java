package com.njit.course.dao;

import com.njit.framework.domain.course.CoursePub;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dustdawn
 * @date 2019/12/8 22:52
 */
public interface CoursePubRepository extends JpaRepository<CoursePub,String> {
}
