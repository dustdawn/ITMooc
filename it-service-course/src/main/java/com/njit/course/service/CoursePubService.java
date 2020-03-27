package com.njit.course.service;

import com.alibaba.fastjson.JSON;
import com.njit.course.client.CmsPageClient;
import com.njit.course.dao.CourseBaseRepository;
import com.njit.course.dao.CoursePubRepository;
import com.njit.framework.domain.cms.CmsPage;
import com.njit.framework.domain.cms.response.CmsPageResult;
import com.njit.framework.domain.cms.response.CmsPostPageResult;
import com.njit.framework.domain.course.CourseBase;
import com.njit.framework.domain.course.CoursePic;
import com.njit.framework.domain.course.CoursePub;
import com.njit.framework.domain.course.ext.TeachplanNode;
import com.njit.framework.domain.course.response.CourseCode;
import com.njit.framework.domain.course.response.CoursePublishResult;
import com.njit.framework.exception.ExceptionCast;
import com.njit.framework.model.response.CommonCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * @author dustdawn
 * @date 2020/3/26 21:24
 */
@Service
public class CoursePubService {

    @Autowired
    private CmsPageClient cmsPageClient;
    @Autowired
    private CourseBaseService courseBaseService;
    @Autowired
    private TeachplanMediaService teachplanMediaService;
    @Autowired
    private CoursePicService coursePicService;
    @Autowired
    private TeachplanService teachplanService;

    @Autowired
    private CoursePubRepository coursePubRepository;
    @Autowired
    private CourseBaseRepository courseBaseRepository;



    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;


    /**
     * 课程预览
     * @param id
     * @return
     */
    public CoursePublishResult preview(String id) {
        CourseBase courseBase = courseBaseService.getCourseBaseById(id);
        if (courseBase == null) {
            ExceptionCast.cast(CourseCode.COURSE_GET_NOTEXISTS);
        }

        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageAliase(courseBase.getName());
        // 唯一索引
        cmsPage.setPageName(id + ".html");
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setPageWebPath(publish_page_webpath);
        //设置dataUrl为访问课程管理的/course/courseview/的接口
        // 由cmsPage服务静态化时调取获取模型数据
        cmsPage.setDataUrl(publish_dataUrlPre + id);
        // 保存到服务器物理地址相对路径
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        //课程页面模板
        cmsPage.setTemplateId(publish_templateId);


        //远程请求cms添加页面
        CmsPageResult cmsPageResult = cmsPageClient.saveCmsPage(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }

        CmsPage cmsPageClient = cmsPageResult.getCmsPage();
        String pageId = cmsPageClient.getPageId();
        courseBase.setStatus("202002");
        courseBaseRepository.save(courseBase);
        //拼装页面的预览url
        String url = previewUrl + pageId;
        return new CoursePublishResult(CommonCode.SUCCESS, url);
    }

    /**
     * 课程发布
     * @param id
     * @return
     */
    @Transactional
    public CoursePublishResult publish(String id) {
        CourseBase courseBase = courseBaseService.getCourseBaseById(id);
        if (courseBase == null) {
            ExceptionCast.cast(CourseCode.COURSE_GET_NOTEXISTS);
        }

        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageAliase(courseBase.getName());
        // 唯一索引
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setPageName(id + ".html");
        cmsPage.setPageWebPath(publish_page_webpath);

        cmsPage.setDataUrl(publish_dataUrlPre + id);

        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        //课程页面模板
        cmsPage.setTemplateId(publish_templateId);
        //远程调用cms服务接口
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        if (!cmsPostPageResult.isSuccess()) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        //更改课程状态
        CourseBase courseNew = this.saveCoursePubState(id);
        if (courseNew == null) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        //保存课程索引信息
        CoursePub coursePub = createCoursePub(id);
        //保存到数据库
        saveCoursePub(id, coursePub);

        //缓存课程的信息

        //得到页面的url
        String pageUrl = cmsPostPageResult.getPageUrl();

        //向teachplanMedia中保存课程媒资信息
        teachplanMediaService.saveTeachplanMediaPub(id);
        return new CoursePublishResult(CommonCode.SUCCESS, pageUrl);
    }



    //课程索引对象
    private CoursePub createCoursePub(String id) {
        CoursePub coursePub = new CoursePub();
        //根据课程id查询course_base
        CourseBase courseBase = courseBaseService.getCourseBaseById(id);
        if (courseBase != null) {
            //将courseBase属性拷贝到coursePub
            BeanUtils.copyProperties(courseBase, coursePub);
        }

        CoursePic coursePic = coursePicService.listCoursePic(id);
        if (coursePic != null) {
            //将coursePic属性拷贝到coursePub
            BeanUtils.copyProperties(coursePic, coursePub);
        }


        TeachplanNode teachplanNode = teachplanService.findTeachplanList(id);
        String jsonString = JSON.toJSONString(teachplanNode);
        coursePub.setTeachplan(jsonString);
        return coursePub;
    }

    /**
     * 将coursePub保存到数据库
     * @param id
     * @param coursePub
     * @return
     */
    private CoursePub saveCoursePub(String id, CoursePub coursePub) {
        Optional<CoursePub> optional = coursePubRepository.findById(id);
        CoursePub coursePubNew = optional.orElse(new CoursePub());

        BeanUtils.copyProperties(coursePub, coursePubNew);
        //可能空值覆盖id
        coursePubNew.setId(id);
        //时间戳，给logstach使用
        coursePubNew.setTimestamp(new Date());
        //发布时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        coursePubNew.setPubTime(date);
        coursePubRepository.save(coursePubNew);
        return coursePubNew;


    }

    /**
     * 更改课程状态为已发布 202003已发布
     * @param courseId
     * @return
     */
    private CourseBase saveCoursePubState(String courseId) {
        CourseBase courseBaseById = courseBaseService.getCourseBaseById(courseId);
        courseBaseById.setStatus("202003");
        courseBaseRepository.save(courseBaseById);
        return courseBaseById;
    }
}
