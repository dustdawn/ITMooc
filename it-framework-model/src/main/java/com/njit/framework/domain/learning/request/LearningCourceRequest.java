package com.njit.framework.domain.learning.request;

import com.njit.framework.model.request.RequestData;
import lombok.Data;
import lombok.ToString;

/**
 * @author dustdawn
 * @date 2020/4/22 12:26
 */
@Data
@ToString
public class LearningCourceRequest  extends RequestData {
    private String userId;
}
