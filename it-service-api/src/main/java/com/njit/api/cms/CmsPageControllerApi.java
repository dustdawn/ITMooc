package com.njit.api.cms;

import com.njit.framework.domain.cms.CmsPage;
import com.njit.framework.domain.cms.request.QueryPageRequest;
import com.njit.framework.domain.cms.response.CmsPageResult;
import com.njit.framework.domain.cms.response.CmsPostPageResult;
import com.njit.framework.model.response.QueryResponseResult;
import com.njit.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author dustdawn
 * @date 2019/11/12 10:05
 */
@Api(value = "cms页面管理接口", description = "cms页面管理接口，提供页面的增删改查")
public interface CmsPageControllerApi {
    /**
     * 页面查询
     * @param page
     * @param size
     * @param queryPageRequest
     * @return
     */
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value = "页码",required=true,paramType="path",dataType="int"),
            @ApiImplicitParam(name="size",value = "每页记录数",required=true,paramType="path",dataType="int")
                    })
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    /**
     * 新增页面
     * @param cmsPage
     * @return
     */
    @ApiOperation("新增页面")
    public CmsPageResult add(CmsPage cmsPage);

    /**
     * 根据id查询页面信息
     * @param id
     * @return
     */
    @ApiOperation("获取页面")
    public CmsPage findById(String id);

    /**
     * 修改页面
     * @param id
     * @param cmsPage
     * @return
     */
    @ApiOperation("修改页面")
    public CmsPageResult edit(String id, CmsPage cmsPage);

    /**
     * 删除页面
     * @param id
     * @return
     */
    @ApiOperation("删除页面")
    public ResponseResult delete(String id);

    /**
     * 发布页面
     * @param id
     * @return
     */
    @ApiOperation("发布页面")
    public ResponseResult post(String id);

    /**
     * 保存页面
     * @param cmsPage
     * @return
     */
    @ApiOperation("保存页面")
    public CmsPageResult save(CmsPage cmsPage);

    /**
     * 一键发布页面
     * @param cmsPage
     * @return
     */
    @ApiOperation("一键发布")
    public CmsPostPageResult postPageQuick(CmsPage cmsPage);

}
