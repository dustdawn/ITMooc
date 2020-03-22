package com.njit.api.filesystem;

import com.njit.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author dustdawn
 * @date 2019/12/11 17:34
 */
@Api(value = "文件管理接口",description = "文件管理接口")
public interface FileSystemControllerApi {
    /**
     * 文件上传
     * @param multipartFile
     * @param metaData json数据
     * @return
     */
    @ApiOperation("上传文件接口")
    public UploadFileResult upload(MultipartFile multipartFile,
                                   String metaData);
}
