package com.njit.framework.model.response;

import lombok.Data;
import lombok.ToString;

/**
 * 查询响应结果集
 * @author dustdawn
 * @date 2020/3/15 22:33
 */

@Data
@ToString
public class QueryResponseResult<T> extends ResponseResult {

    /**
     * 查询结果集
     */
    QueryResult<T> queryResult;

    public QueryResponseResult(ResultCode resultCode, QueryResult queryResult){
        super(resultCode);
       this.queryResult = queryResult;
    }

}
