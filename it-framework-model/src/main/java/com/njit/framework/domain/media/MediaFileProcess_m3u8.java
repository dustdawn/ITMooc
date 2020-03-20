package com.njit.framework.domain.media;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author dustdawn
 * @date 2020/3/20 11:10
 */
@Data
@ToString
public class MediaFileProcess_m3u8 extends MediaFileProcess {

    //ts列表
    private List<String> tslist;

}
