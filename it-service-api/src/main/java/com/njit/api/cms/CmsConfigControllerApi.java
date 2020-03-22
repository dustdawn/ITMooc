package com.njit.api.cms;

import com.njit.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author dustdawn
 * @date 2019/12/1 16:08
 */
@Api(value="cms配置管理接口",description = "cms配置管理接口，提供数据模型的管理、查询接口")
public interface CmsConfigControllerApi {
    /**
     * 根据id查询模型数据
     * @param id
     * @return
     */
    @ApiOperation("根据id查询CMS配置信息")
    public CmsConfig getModel(String id);
}
