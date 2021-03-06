package com.njit.cms;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 18:11
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFsTest {

    @Autowired
    GridFsTemplate gridFsTemplate;
    /**
     * 创建该对象要配置config，指定数据库
     */
    @Autowired
    GridFSBucket gridFSBucket;

    //存文件
    @Test
    public void testStore() throws FileNotFoundException {
        //定义file
        File file =new File("d:/ITMooc/Template/courseDetail.ftl");
        //定义fileInputStream
        FileInputStream fileInputStream = new FileInputStream(file);
        //存储fs_chinks表里
        ObjectId objectId = gridFsTemplate.store(fileInputStream, "courseDetail.ftl");
        System.out.println(objectId);
        // objectId对应fs.files中的id，fs.chunk中的files_id
    }

    //存文件
    @Test
    public void testStore2() throws FileNotFoundException {
        //定义file
        File file =new File("d:/ITMooc/Template/index_banner.ftl");
        //定义fileInputStream
        FileInputStream fileInputStream = new FileInputStream(file);
        //存储fs_chinks表里
        ObjectId objectId = gridFsTemplate.store(fileInputStream, "index_banner.ftl");
        System.out.println(objectId);
        // objectId对应fs.files中的id，fs.chunk中的files_id
    }

    //存文件
    @Test
    public void testStore3() throws FileNotFoundException {
        //定义file
        File file =new File("d:/ITMooc/Template/hot_course.ftl");
        //定义fileInputStream
        FileInputStream fileInputStream = new FileInputStream(file);
        //存储fs_chinks表里
        ObjectId objectId = gridFsTemplate.store(fileInputStream, "hot_course.ftl");
        System.out.println(objectId);
        // objectId对应fs.files中的id，fs.chunk中的files_id
    }

    //存文件
    @Test
    public void testStore4() throws FileNotFoundException {
        //定义file
        File file =new File("d:/ITMooc/Template/java_course.ftl");
        //定义fileInputStream
        FileInputStream fileInputStream = new FileInputStream(file);
        //存储fs_chinks表里
        ObjectId objectId = gridFsTemplate.store(fileInputStream, "java_course.ftl");
        System.out.println(objectId);
        // objectId对应fs.files中的id，fs.chunk中的files_id
    }

    //存文件
    @Test
    public void testStore5() throws FileNotFoundException {
        //定义file
        File file =new File("d:/ITMooc/Template/c_course.ftl");
        //定义fileInputStream
        FileInputStream fileInputStream = new FileInputStream(file);
        //存储fs_chinks表里
        ObjectId objectId = gridFsTemplate.store(fileInputStream, "c_course.ftl");
        System.out.println(objectId);
        // objectId对应fs.files中的id，fs.chunk中的files_id
    }


    //取文件
    @Test
    public void queryFile() throws IOException {
        //根据文件id查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5e7b592b46465042ec822f65")));
        //打开一个下载流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建GridFsResource对象，获取流
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
        //从流中取数据
        String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        System.out.println(content);

    }

}
