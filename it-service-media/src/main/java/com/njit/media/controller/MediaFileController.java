package com.njit.media.controller;

import com.njit.api.media.MediaFileControllerApi;
import com.njit.framework.domain.media.MediaFile;
import com.njit.framework.domain.media.request.QueryMediaFileRequest;
import com.njit.framework.model.response.QueryResponseResult;
import com.njit.framework.model.response.ResponseResult;
import com.njit.media.service.MediaFileService;
import com.njit.media.service.MediaUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * @author dustdawn
 * @date 2020/3/6 21:21
 */
@RestController
@RequestMapping("/media/file")
public class MediaFileController implements MediaFileControllerApi {
    @Autowired
    MediaFileService mediaFileService;
    @Autowired
    MediaUploadService mediaUploadService;

    /**
     * 媒资文件查询
     * @param page
     * @param size
     * @param queryMediaFileRequest
     * @return
     */
    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult<MediaFile> findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryMediaFileRequest queryMediaFileRequest) {
        return mediaFileService.findList(page,size,queryMediaFileRequest);
    }

    @Override
    @GetMapping("/process/{id}")
    public ResponseResult process(@PathVariable("id") String mediaId) {
        return mediaUploadService.sendProcessVideoMsg(mediaId);
    }
}
