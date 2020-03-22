package com.njit.framework.domain.cms.response;

import com.njit.framework.domain.cms.CmsPage;
import com.njit.framework.model.response.ResponseResult;
import com.njit.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dustdawn
 * @date 2020/3/22 21:58
 */
@Data
@NoArgsConstructor
public class CmsPageResult extends ResponseResult {
    CmsPage cmsPage;
    public CmsPageResult(ResultCode resultCode, CmsPage cmsPage) {
        super(resultCode);
        this.cmsPage = cmsPage;
    }
}
