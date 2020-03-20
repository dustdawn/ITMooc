package com.njit.framework.domain.media.request;

import com.njit.framework.model.request.RequestData;
import lombok.Data;

/**
 * 媒资文件查询请求对象
 * @author dustdawn
 * @date 2020/3/20 11:14
 */
@Data
public class QueryMediaFileRequest extends RequestData {

    private String fileOriginalName;
    private String processStatus;
    private String tag;
}
