package com.njit.framework.domain.learning.response;

import com.njit.framework.model.response.ResponseResult;
import com.njit.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author dustdawn
 * @date 2020/3/8 22:09
 */
@Data
@ToString
@NoArgsConstructor
public class GetMediaResult extends ResponseResult {
    //视频播放地址
    String fileUrl;


    public GetMediaResult(ResultCode resultCode, String fileUrl) {
        super(resultCode);
        this.fileUrl = fileUrl;
    }
}
