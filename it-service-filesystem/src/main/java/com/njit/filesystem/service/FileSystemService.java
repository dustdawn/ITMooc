package com.njit.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.njit.filesystem.dao.FileSystemRepository;
import com.njit.framework.domain.filesystem.FileSystem;
import com.njit.framework.domain.filesystem.response.FileSystemCode;
import com.njit.framework.domain.filesystem.response.UploadFileResult;
import com.njit.framework.exception.ExceptionCast;
import com.njit.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author dustdawn
 * @date 2019/12/11 17:42
 */
@Service
public class FileSystemService {
    @Value("${itmooc.fastdfs.tracker_servers}")
    private String tracker_servers;
    @Value("${itmooc.fastdfs.connect_timeout_in_seconds}")
    private int connect_timeout_in_seconds;
    @Value("${itmooc.fastdfs.network_timeout_in_seconds}")
    private int network_timeout_in_seconds;
    @Value("${itmooc.fastdfs.charset}")
    private String charset;

    @Autowired
    private FileSystemRepository fileSystemRepository;


    public UploadFileResult upload(MultipartFile multipartFile, String metaData) {
        if (multipartFile == null) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        //1.将文件上传到fastDFS中，得到文件id
        String fileId = this.fdfsUpload(multipartFile);
        //group1/M00/00/00/wKgOgV3w-nKAY0IZAAAQJJE6fV0854.jpg
        if (StringUtils.isEmpty(fileId)) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }

        //2.将文件id及其他文件信息存储到mongodb中
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setFilePath(fileId);
        fileSystem.setFileSize(multipartFile.getSize());
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        fileSystem.setFileType(multipartFile.getContentType());
        if (StringUtils.isNotEmpty(metaData)) {
            try {
                Map metaDataMap = JSON.parseObject(metaData, Map.class);
                fileSystem.setMetadata(metaDataMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        fileSystemRepository.save(fileSystem);


        return new UploadFileResult(CommonCode.SUCCESS, fileSystem);
    }

    /**
     * 将文件上传到fastDFS中，得到文件id
     * @param multipartFile
     * @return
     */
    private String fdfsUpload(MultipartFile multipartFile) {
        //加载配置文件
        this.initFastDFSConfig();
        //定义TrackerClient
        TrackerClient trackerClient = new TrackerClient();
        //连接tracker，获取trackerServer
        try {
            TrackerServer trackerServer = trackerClient.getConnection();
            //通过tracker服务器获取storageServer
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            //通过trackerServer和storageServer创建storageClient
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
            //上传文件
            //获取扩展名
            String originalFilename = multipartFile.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileId = storageClient1.upload_file1(multipartFile.getBytes(), ext, null);
            return fileId;
        } catch (IOException | MyException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void initFastDFSConfig() {
        try {
            //初始化tracker服务地址，多个逗号分隔
            ClientGlobal.initByTrackers(tracker_servers);
            ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
            ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
            ClientGlobal.setG_charset(charset);
        } catch (Exception e) {
            e.printStackTrace();
            //文件初始化失败
            ExceptionCast.cast(FileSystemCode.FS_INITFDFSERROR);
        }
    }


}
