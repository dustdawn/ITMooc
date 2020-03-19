package com.njit.api.sys;

import com.njit.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author dustdawn
 * @date 2019/12/9 18:43
 */
@Api(value = "数据字典接口",description = "提供数据字典接口的管理、查询功能")
public interface SysDictionaryControllerApi {
    /**
     * 数据字典查询
     * @param type
     * @return
     */
    @ApiOperation(value="数据字典查询接口")
    public SysDictionary getByType(String type);
}
