package com.njit.framework.domain.media.response;

import com.njit.framework.model.response.ResponseResult;
import com.njit.framework.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 确认分块响应结构
 * @author dustdawn
 * @date 2020/3/20 11:17
 */
@Data
@ToString
@NoArgsConstructor
public class CheckChunkResult extends ResponseResult {

    public CheckChunkResult(ResultCode resultCode, boolean fileExist) {
        super(resultCode);
        this.fileExist = fileExist;
    }
    @ApiModelProperty(value = "文件分块存在标记", example = "true", required = true)
    boolean fileExist;
}