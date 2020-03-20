package com.njit.media.service;

import com.alibaba.fastjson.JSON;
import com.njit.framework.client.RabbitMQList;
import com.njit.framework.domain.media.MediaFile;
import com.njit.framework.domain.media.response.CheckChunkResult;
import com.njit.framework.domain.media.response.MediaCode;
import com.njit.framework.exception.ExceptionCast;
import com.njit.framework.model.response.CommonCode;
import com.njit.framework.model.response.ResponseResult;
import com.njit.media.dao.MediaFileRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * @author dustdawn
 * @date 2020/3/3 23:24
 */
@Service
public class MediaUploadService {
    @Autowired
    MediaFileRepository mediaFileRepository;

    @Value("${it-service-media.upload-location}")
    String upload_location;

    @Value("${it-service-media.mq.routingkey-media-video}")
    String routingkey;

    @Autowired
    RabbitTemplate rabbitTemplate;

    //得到文件夹路径
    private String getFileFolderPath(String fileMd5) {
        return upload_location + fileMd5.substring(0,1) + "/" + fileMd5.substring(1,2) + "/" + fileMd5 + "/";
    }
    //得到文件路径
    private String getFilePath(String fileMd5, String fileExt) {
        return upload_location + fileMd5.substring(0,1) + "/" + fileMd5.substring(1,2) + "/" + fileMd5 + "/" + fileMd5 + "." + fileExt;
    }
    //得到块文件所属目录
    private String getChunkFileFolderPath(String fileMd5) {
        return upload_location + fileMd5.substring(0,1) + "/" + fileMd5.substring(1,2) + "/" + fileMd5 + "/chunk/";
    }


    /**
     * @param fileMd5 文件md5值
     * @param fileExt 文件扩展名
     * @return 文件路径 */

    /**
     * 文件上传之前的注册
     * 根据文件md5得到文件路径
     * 规则：
     * 一级目录：md5的第一个字符
     * 二级目录：md5的第二个字符
     * 三级目录：md5
     * 文件名：md5+文件扩展名
     * @param fileMd5
     * @param fileName
     * @param fileSize
     * @param mimetype
     * @param fileExt
     * @return
     */
    public ResponseResult register(String fileMd5,
                                   String fileName,
                                   Long fileSize,
                                   String mimetype,
                                   String fileExt) {
        //1.检查文件在磁盘上是否存在
        String fileFolderPath = this.getFileFolderPath(fileMd5);
        String filePath = this.getFilePath(fileMd5, fileExt);
        File file = new File(filePath);
        //文件是否存在
        boolean exists = file.exists();


        //2.检查文件信息在mongd中是否存在
        Optional<MediaFile> mediaFileOptional = mediaFileRepository.findById(fileMd5);
        if (exists && mediaFileOptional.isPresent()) {
            //文件存在
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }
        //文件不存在时做一些准备工作，检查文件所在目录是否存在，如不存在则创建
        File fileFolder = new File(fileFolderPath);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     *
     * @param fileMd5 文件md5
     * @param chunk 块的下标
     * @param chunkSize 块的大小
     * @return
     */
    public CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        //检查分块文件是否存在
        //得到分块文件所在目录
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        File chunkFile = new File(chunkFileFolderPath + chunk);
        if (chunkFile.exists()) {
            //块文件存在
            return new CheckChunkResult(CommonCode.SUCCESS, true);
        } else {
            //块文件不存在
            return new CheckChunkResult(CommonCode.SUCCESS, false);
        }
    }

    /**
     * 上传分块
     * @param file
     * @param fileMd5
     * @param chunk
     * @return
     */
    public ResponseResult uploadchunk(MultipartFile file, String fileMd5, Integer chunk) {
        //检查分块目录，如果不存在则自动创建
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        //得到分块路径
        String chunkFilePath = chunkFileFolderPath + chunk;

        File chunkFileFolder = new File(chunkFileFolderPath);
        //如果不存在则要自动创建
        if (!chunkFileFolder.exists()) {
            chunkFileFolder.mkdirs();
        }
        //得到上传文件的输入
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = file.getInputStream();
            outputStream = new FileOutputStream(new File(chunkFilePath));
            //将输入流数据拷贝到输出流
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 合并分块
     * @param fileMd5
     * @param fileName
     * @param fileSize
     * @param mimetype
     * @param fileExt
     * @return
     */
    public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        //1.合并所有分块
        //得到分块所属的目录
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        File chunkFileFolder = new File(chunkFileFolderPath);
        //分块文件列表
        File[] files = chunkFileFolder.listFiles();
        List<File> chunkFileList = Arrays.asList(files);


        //创建合并后的文件
        String filePath = this.getFilePath(fileMd5,fileExt);
        File mergeFile = new File(filePath);

        //得到分块合并后文件
        mergeFile = this.mergeFile(chunkFileList, mergeFile);
        if (mergeFile == null) {
            //合并文件失败
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
        }

        //2.校验文件的md5只是否和前端传入的md5一致

        boolean checkFileMd5 = this.checkFileMd5(mergeFile, fileMd5);
        if (!checkFileMd5) {
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }

        //3.将文件的信息写入mongodb
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileOriginalName(fileName);
        mediaFile.setFileName(fileMd5 + "." + fileExt);
        //文件路径保存相对路径
        String filePath1 = fileMd5.substring(0,1) + "/" + fileMd5.substring(1,2) + "/" + fileMd5 + "/";
        mediaFile.setFilePath(filePath1);
        mediaFile.setFileSize(fileSize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType(mimetype);
        mediaFile.setFileType(fileExt);
        //处理状态设置为未处理
        mediaFile.setProcessStatus("303004");

        //状态设置为上传成功
        mediaFile.setFileStatus("301002");
        mediaFileRepository.save(mediaFile);

        //向MQ发送视频处理消息
        sendProcessVideoMsg(mediaFile.getFileId());
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //得到分块合并后文件
    private File mergeFile(List<File> chunkFileList, File mergeFile) {
        try {
            //合并文件存在则删除
            if (mergeFile.exists()) {

                mergeFile.delete();
            } else {
                //创建一个新文件

                mergeFile.createNewFile();
            }

            //对块文件进行排序
            Collections.sort(chunkFileList, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                        return 1;
                    }
                    return -1;
                }
            });

            //创建一个写对象
            RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");
            //缓冲区
            byte[] b = new byte[1024];
            for (File chunkFile : chunkFileList) {
                RandomAccessFile raf_read = new RandomAccessFile(chunkFile, "r");
                int len = -1;
                while ((len = raf_read.read(b)) != -1) {
                    raf_write.write(b, 0, len);
                }
                raf_read.close();
            }
            raf_write.close();
            return mergeFile;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean checkFileMd5(File mergeFile, String md5) {
        try {
            //创建文件输入流
            FileInputStream fileInputStream = new FileInputStream(mergeFile);

            //得到文件的md
            //apache工具类
            String md5Hex = DigestUtils.md5Hex(fileInputStream);

            //和传入的md5作比较
            if (md5.equalsIgnoreCase(md5Hex)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 发送视频处理消息
     * @param mediaId 文件id
     * @return
     */
    public ResponseResult sendProcessVideoMsg(String mediaId) {
        //查询数据库mediaFile
        Optional<MediaFile> mediaFileOptional = mediaFileRepository.findById(mediaId);
        if (!mediaFileOptional.isPresent()) {
            //查询不存在
            ExceptionCast.cast(CommonCode.FAIL);
        }


        //构建消息内容
        Map<String, String> map = new HashMap<>();
        map.put("mediaId", mediaId);
        String jsonString = JSON.toJSONString(map);
        //向mq发送视频处理消息
        try {
            rabbitTemplate.convertAndSend(RabbitMQList.EXCHANGES_MEDIA_PROCESSOR, routingkey, jsonString);
        } catch (AmqpException e) {
            e.printStackTrace();
            return new ResponseResult(CommonCode.FAIL);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
