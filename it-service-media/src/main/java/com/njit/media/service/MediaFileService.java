package com.njit.media.service;

import com.njit.framework.domain.media.MediaFile;
import com.njit.framework.domain.media.request.QueryMediaFileRequest;
import com.njit.framework.model.response.CommonCode;
import com.njit.framework.model.response.QueryResponseResult;
import com.njit.framework.model.response.QueryResult;
import com.njit.media.dao.MediaFileRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dustdawn
 * @date 2020/3/6 21:22
 */
@Service
public class MediaFileService {

    @Autowired
    MediaFileRepository mediaFileRepository;
    /**
     * 查询我的媒资列表
     * @param page
     * @param size
     * @param queryMediaFileRequest
     * @return
     */
    public QueryResponseResult<MediaFile> findList(int page, int size, QueryMediaFileRequest queryMediaFileRequest) {
        if (queryMediaFileRequest == null) {
            queryMediaFileRequest = new QueryMediaFileRequest();
        }
        //条件值对象
        MediaFile mediaFile = new MediaFile();
        if (StringUtils.isNotEmpty(queryMediaFileRequest.getFileOriginalName())) {
            mediaFile.setFileOriginalName(queryMediaFileRequest.getFileOriginalName());
        }

        if (StringUtils.isNotEmpty(queryMediaFileRequest.getProcessStatus())) {
            mediaFile.setProcessStatus(queryMediaFileRequest.getProcessStatus());
        }

        if (StringUtils.isNotEmpty(queryMediaFileRequest.getTag())) {
            mediaFile.setTag(queryMediaFileRequest.getTag());
        }


        //条件匹配器，默认精确匹配exact，模糊匹配 tag和fileOriginalName
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                                        .withMatcher("tag", ExampleMatcher.GenericPropertyMatchers.contains())
                                        .withMatcher("fileOriginalName",ExampleMatcher.GenericPropertyMatchers.contains());

        //定义条件对象
        Example<MediaFile> example = Example.of(mediaFile, exampleMatcher);
        //分页查询对象
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        Pageable pageable = PageRequest.of(page, size);
        //分页查询
        Page<MediaFile> all = mediaFileRepository.findAll(example, pageable);
        //查询总记录数
        long total = all.getTotalElements();
        //数据列表
        List<MediaFile> content = all.getContent();
        //返回的数据集
        QueryResult<MediaFile> queryResult = new QueryResult<>();
        queryResult.setTotal(total);
        queryResult.setList(content);

        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }
}
