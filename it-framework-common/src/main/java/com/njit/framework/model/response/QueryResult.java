package com.njit.framework.model.response;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 查询结果集
 * @author dustdawn
 * @date 2020/3/15 22:33
 */
@Data
@ToString
public class QueryResult<T> {
    /**
     * 结果数据集
     */
    private List<T> list;
    /**
     * 结果集数据总数
     */
    private long total;
}
