package com.njit.api.media;

import com.njit.framework.domain.media.MediaFile;
import com.njit.framework.domain.media.request.QueryMediaFileRequest;
import com.njit.framework.model.response.QueryResponseResult;
import com.njit.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author dustdawn
 * @date 2020/3/6 21:17
 */
@Api(value = "媒资文件管理接口",description = "媒资列表，")
public interface MediaFileControllerApi {
    @ApiOperation("我的媒资文件查询列表")
    public QueryResponseResult<MediaFile> findList(int page, int size, QueryMediaFileRequest queryMediaFileRequest);

    @ApiOperation("开始处理某个文件")
    public ResponseResult process(String mediaId);
}
