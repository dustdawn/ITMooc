package com.njit.api.cms;

import com.njit.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author dustdawn
 * @date 2019/12/1 16:08
 */
@Api(value="cms模板管理接口",description = "cms模板管理接口，提供模板的管理、查询接口")
public interface CmsTemplateControllerApi {
    /**
     * 根据id查询站点
     * @return
     */
    @ApiOperation("查询所有模板")
    public QueryResponseResult findList();
}
