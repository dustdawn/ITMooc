package com.njit.framework.domain.cms.response;

import com.njit.framework.model.response.ResponseResult;
import com.njit.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 页面预览响应结构
 * @author dustdawn
 * @date 2020/3/22 22:00
 */
@Data
@ToString
@NoArgsConstructor
public class CoursePreviewResult extends ResponseResult {
    public CoursePreviewResult(ResultCode resultCode, String url) {
        super(resultCode);
        this.url = url;
    }

    String url;
}
