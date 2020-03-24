package com.njit.media_processor.mq;

import com.alibaba.fastjson.JSON;
import com.njit.framework.client.RabbitMQList;
import com.njit.framework.domain.media.MediaFile;
import com.njit.framework.domain.media.MediaFileProcess_m3u8;
import com.njit.framework.util.HlsVideoUtil;
import com.njit.framework.util.Mp4VideoUtil;
import com.njit.media_processor.dao.MediaFileRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author dustdawn
 * @date 2020/3/5 22:08
 */
@Component
public class MediaProcessTask {

    @Value("${it-service-media.ffmpeg-path}")
    String ffmpegPath;
    //上传文件根目录
    @Value("${it-service-media.video-location}")
    String serverPath;

    @Autowired
    MediaFileRepository mediaFileRepository;

    /**
     * 接收视频处理消息进行视频处理
     * @param msg
     */
    @RabbitListener(queues = RabbitMQList.QUEUE_MEDIA_PROCESSOR, containerFactory = "customContainerFactory")
    public void receiveMediaProcessTask(String msg) {
        //1.解析消息内容，得到mediaId
        Map map = JSON.parseObject(msg, Map.class);
        String mediaId = (String) map.get("mediaId");
        //2.根据mediaId从数据库中查询文件信息
        Optional<MediaFile> mediaFileOptional = mediaFileRepository.findById(mediaId);
        if (!mediaFileOptional.isPresent()) {
            return;
        }
        MediaFile mediaFile = mediaFileOptional.get();
        String fileType = mediaFile.getFileType();

        //需要处理状态改为处理中
        mediaFile.setProcessStatus("303001");
        mediaFileRepository.save(mediaFile);


        //3.使用工具类将视频文件生成mp4
        //要处理的视频文件的文件名称
        String video_path = serverPath + mediaFile.getFilePath() + mediaFile.getFileName();
        //生成的mp4文件名称
        String mp4_name = mediaFile.getFileId() + ".mp4";
        //生成的mp4所在的路径
        String mp4folder_path = serverPath + mediaFile.getFilePath();


        if (fileType.equalsIgnoreCase("mp4")) {

            //创建工具类对象
            Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpegPath,video_path,mp4_name,mp4folder_path);
            //进行处理
            String result = mp4VideoUtil.generateMp4();
            if (result == null || !result.equals("success")) {
                //处理失败
                mediaFile.setProcessStatus("303003");
                //定义处理失败原因mediaFileProcess_m3u8
                MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
                mediaFileProcess_m3u8.setErrormsg(result);
                mediaFileRepository.save(mediaFile);

                return;
            }
        }


        //4.将mp4生成m3u8和ts文件
        //mp4视频文件路径
        String mp4_video_path = serverPath + mediaFile.getFilePath() + mp4_name;
        //m3u8文件名
        String m3u8_name = mediaFile.getFileId() + ".m3u8";
        //m3u8所在文件目录
        String m3u8_path = serverPath + mediaFile.getFilePath() + "hls/";
        HlsVideoUtil hlsVideoUtil = new HlsVideoUtil(ffmpegPath,mp4_video_path,m3u8_name,m3u8_path);
        //生成m3u8和ts文件
        String tsResult = hlsVideoUtil.generateM3u8();
        if (tsResult == null || !tsResult.equals("success")) {
            //处理失败
            mediaFile.setProcessStatus("303003");
            //定义处理失败原因mediaFileProcess_m3u8
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg(tsResult);
            mediaFileRepository.save(mediaFile);
            return;
        }

        //处理成功
        //获取ts文件列表
        List<String> ts_list = hlsVideoUtil.get_ts_list();
        mediaFile.setProcessStatus("303002");
        MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
        mediaFileProcess_m3u8.setTslist(ts_list);
        mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);

        //保存fileUrl（此路径就是视频播放的相对路径）
        String fileUrl = mediaFile.getFilePath() + "hls/" + m3u8_name;
        mediaFile.setFileUrl(fileUrl);
        mediaFileRepository.save(mediaFile);

    }
}
