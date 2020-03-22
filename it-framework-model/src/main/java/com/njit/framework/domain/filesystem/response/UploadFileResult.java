package com.njit.framework.domain.filesystem.response;

import com.njit.framework.domain.filesystem.FileSystem;
import com.njit.framework.model.response.ResponseResult;
import com.njit.framework.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author dustdawn
 * @date 2020/3/21 21:57
 */
@Data
@ToString
public class UploadFileResult extends ResponseResult {
    @ApiModelProperty(value = "文件信息", example = "true", required = true)
    FileSystem fileSystem;

    public UploadFileResult(ResultCode resultCode, FileSystem fileSystem) {
        super(resultCode);
        this.fileSystem = fileSystem;
    }

}