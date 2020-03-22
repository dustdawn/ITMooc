package com.njit.filesystem.controller;

import com.njit.api.filesystem.FileSystemControllerApi;
import com.njit.filesystem.service.FileSystemService;
import com.njit.framework.domain.filesystem.response.UploadFileResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author dustdawn
 * @date 2019/12/11 22:07
 */
@RestController
@RequestMapping("/filesystem")
public class FileSystemController implements FileSystemControllerApi {
    @Autowired
    private FileSystemService fileSystemService;
    @Override
    @PostMapping("/upload")
    public UploadFileResult upload(MultipartFile multipartFile, String metaData) {
        return fileSystemService.upload(multipartFile,metaData);
    }
}
