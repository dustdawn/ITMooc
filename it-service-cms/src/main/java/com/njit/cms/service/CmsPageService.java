package com.njit.cms.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.njit.cms.dao.CmsPageRepository;
import com.njit.cms.dao.CmsSiteRepository;
import com.njit.cms.dao.CmsTemplateRepository;
import com.njit.framework.domain.cms.CmsPage;
import com.njit.framework.domain.cms.CmsSite;
import com.njit.framework.domain.cms.CmsTemplate;
import com.njit.framework.domain.cms.request.QueryPageRequest;
import com.njit.framework.domain.cms.response.CmsCode;
import com.njit.framework.domain.cms.response.CmsPageResult;
import com.njit.framework.domain.cms.response.CmsPostPageResult;
import com.njit.framework.exception.ExceptionCast;
import com.njit.framework.model.response.CommonCode;
import com.njit.framework.model.response.QueryResponseResult;
import com.njit.framework.model.response.QueryResult;
import com.njit.framework.model.response.ResponseResult;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * @author dustdawn
 * @date 2020/3/22 22:08
 */
@Service
public class CmsPageService {
    @Autowired
    CmsSiteService cmsSiteService;

    @Autowired
    RestTemplate restTemplate;
    /**
     * Mongodb存储文件GridFs
     */
    @Autowired
    private GridFsTemplate gridFsTemplate;
    /**
     * 创建该对象要配置config，指定数据库
     */
    @Autowired
    private GridFSBucket gridFSBucket;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Service
     */
    @Autowired
    private CmsTemplateService cmsTemplateService;

    /**
     * dao
     */
    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;
    @Autowired
    private CmsSiteRepository cmsSiteRepository;


    /**
     * 页面查询方法
     * @param page 页码，从1开始计数
     * @param size 每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {

        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }

        //自定义条件查询
        CmsPage cmsPage = new CmsPage();
        //定义条件匹配器，模糊查询
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());

        //设置条件值
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //设置模板id查询
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //设置页面别名为查询条件
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }

        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);

        //分页参数
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);

        QueryResult queryResult = new QueryResult();
        //数据总记录数
        queryResult.setTotal(all.getTotalElements());
        //数据列表
        queryResult.setList(all.getContent());
        //返回结果集
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    /**
     * 页面添加
     * @param cmsPage
     * @return
     */
    public CmsPageResult add(CmsPage cmsPage) {
        if (null == cmsPage) {
            //抛出非法参数异常，指定异常信息的内容
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //根据唯一索引校验
        CmsPage cmsPageIndex = cmsPageRepository.findBySiteIdAndPageNameAndPageWebPath(cmsPage.getSiteId(), cmsPage.getPageName(), cmsPage.getPageWebPath());

        if (null != cmsPageIndex) {
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        cmsPage.setPageId(null);
        cmsPage.setPageCreateTime(new Date());
        String pageTemplate = cmsTemplateService.getPageTemplateById(cmsPage.getSiteId());
        if (StringUtils.isNotEmpty(pageTemplate)) {
            cmsPage.setPageTemplate(pageTemplate);
        }
        // 修改页面静态化状态为未静态化
        cmsPage.setPageStatus("100001");
        CmsPage save = cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS, save);

    }

    /**
     * 根据页面id获取页面信息
     * @param id
     * @return
     */
    public CmsPage getById(String id) {
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        return optional.orElse(null);
    }

    /**
     * 修改页面信息
     * @param id
     * @param cmsPage
     * @return
     */
    public CmsPageResult update(String id, CmsPage cmsPage) {
        CmsPage byId = this.getById(id);
        if (byId != null) {
            //更新模板id
            byId.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            byId.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            byId.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            byId.setPageName(cmsPage.getPageName());
            //更新访问路径
            byId.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            byId.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            //更新dataUrl
            byId.setDataUrl(cmsPage.getDataUrl());
            if (cmsPage.getPageHtml() != null) {
                byId.setPageHtml(cmsPage.getPageHtml());
            }
            if (cmsPage.getPageStatus() != null) {
                byId.setPageStatus(cmsPage.getPageStatus());
            }
            if (cmsPage.getPubUrl() != null) {
                byId.setPubUrl(cmsPage.getPubUrl());
            }




            cmsPageRepository.save(byId);
            return new CmsPageResult(CommonCode.SUCCESS, byId);
        }
        return new CmsPageResult(CmsCode.CMS_PAGE_NOTEXISTS, null);
    }

    /**
     * 删除页面
     * @param id
     * @return
     */
    public ResponseResult delete(String id) {
        CmsPage byId = this.getById(id);
        if (byId == null) {
            return new ResponseResult(CommonCode.FAIL);
        }
        cmsPageRepository.delete(byId);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 保存或更新页面
     * @param cmsPage
     * @return
     */
    public CmsPageResult save(CmsPage cmsPage) {
        // 判断页面是否存在
        CmsPage cmsPageFind = cmsPageRepository.findBySiteIdAndPageNameAndPageWebPath(cmsPage.getSiteId(), cmsPage.getPageName(), cmsPage.getPageWebPath());
        if (cmsPageFind != null) {
            // 更新
            return this.update(cmsPageFind.getPageId(), cmsPage);
        }
        return this.add(cmsPage);
    }

    /**
     * 页面发布
     * @param pageId
     * @return
     */
    public ResponseResult post(String pageId) {
        // 执行页面静态化
        String pageHtml = this.getPageHtml(pageId);
        if (StringUtils.isEmpty(pageHtml)) {
            return new ResponseResult(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }

        // 将页面静态化文件存储到GridFs中
        CmsPage cmsPage = this.saveHtml(pageId, pageHtml);
        if (cmsPage == null) {
            return new ResponseResult(CmsCode.CMS_GENERATEHTML_SAVEHTMLERROR);
        }
        return this.savePageToServerPath(pageId);
    }

    /**
     * 页面静态方法
     * @param pageId
     * @return 页面html静态页面
     *
     * 1.静态化程序获取页面的DataUrl
     * 2.静态化程序远程请求DataUrl获取数据模型
     * 3.静态化程序获取页面的模板信息
     * 4.执行页面静态化
     */
    public String getPageHtml(String pageId) {
        // 判断页面是否存在以及是否静态化
        CmsPage byId = this.getById(pageId);
        if (byId == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        // 如果已经静态化则返回
        /*if (!"100001".equals(byId.getPageStatus()) && StringUtils.isNotEmpty(byId.getPageHtml())) {
            return byId.getPageHtml();
        }*/
        //获取数据模型
        Map modal = this.getModalByPageId(pageId);
        if (null == modal) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        //获取页面的模板信息
        String templateContent = this.getTemplateByPageId(pageId);
        if (null == templateContent) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //执行静态化
        String pageHtml = this.generateHtml(templateContent, modal);
        //保存静态页面
        // 设置静态化页面文本
        byId.setPageHtml(pageHtml);
        // 修改页面静态化状态为已静态化
        byId.setPageStatus("100002");
        this.save(byId);
        return pageHtml;
    }

    /**
     * 根据页面id获取数据模型
     * @param pageId
     * @return
     */
    private Map getModalByPageId(String pageId) {
        // 获取页面信息
        CmsPage cmsPage = this.getById(pageId);
        if (null == cmsPage) {
            // 页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        // 取出页面的dataUrl
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isEmpty(dataUrl)) {
            // url为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        // 通过restTemplate请求dataUrl获取数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;
    }

    /**
     * 获取页面模板信息
     * @param pageId
     * @return
     */
    private String getTemplateByPageId(String pageId) {
        // 获取页面信息
        CmsPage cmsPage = this.getById(pageId);
        if (null == cmsPage) {
            // 页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            // 页面模板信息为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        // 查询模板信息
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if (optional.isPresent()) {
            CmsTemplate cmsTemplate = optional.get();
            // 获取模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();
            // 从GridFs中取出模板文件内容
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            if (gridFSFile == null) {
                ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
            }
            // 打开一个下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            // 创建GridFsResource对象，获取流
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
            // 从流中取出数据
            try {
                String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        return null;
    }

    /**
     * 页面静态化
     * 通过Freemarker模板引擎
     * @param templateContent 页面模板信息
     * @param modal 数据模型
     * @return
     */
    private String generateHtml(String templateContent, Map modal) {
        // 创建配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 创建模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template", templateContent);
        // 向Configuration配置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        // 获取模板
        try {
            Template template = configuration.getTemplate("template");
            // 调用Api进行静态化
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, modal);
            return content;
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存html到GridFS
     * @param pageId 页面Id
     * @param pageHtml 静态化后的页面
     * @return
     */
    private CmsPage saveHtml(String pageId, String pageHtml) {
        CmsPage cmsPage = this.getById(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        ObjectId objectId = null;
        InputStream inputStream = null;
        try {
            inputStream = IOUtils.toInputStream(pageHtml, "utf-8");
            // 将html文件内容保存到GridFS中,命名为cms对象的pageName
            objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 将html文件id更新到cmsPage中
        if (objectId != null) {
            cmsPage.setHtmlFileId(objectId.toHexString());
        }
        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }


    /**
     * 保存html页面到服务器物理地址
     * @param pageId
     */
    private ResponseResult savePageToServerPath(String pageId) {
        CmsPage cmsPage = this.getById(pageId);
        if (null == cmsPage) {
            return new ResponseResult(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //得到html的文件Id：查询cmsPage获取htmlFileId
        String htmlFileId = cmsPage.getHtmlFileId();
        //从gridFS中查询html文件
        InputStream inputStream = this.getFileById(htmlFileId);
        if (null == inputStream) {
            return new ResponseResult(CmsCode.CMS_GENERATEHTML_HTMLNOTEXISTS);
        }
        //得到站点的物理路径
        CmsSite cmsSite = cmsSiteService.findCmsSiteById(cmsPage.getSiteId());
        if (null == cmsSite) {
            return new ResponseResult(CmsCode.CMS_SITE_NOTEXISTS);
        }
        String sitePhysicalPath = cmsSite.getSitePhysicalPath();
        //得到页面的物理路径
        String pagePath = sitePhysicalPath + cmsPage.getPagePhysicalPath() + cmsPage.getPageName();
        //将html文件保存到服务器物理路径上
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(pagePath));
            IOUtils.copy(inputStream, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cmsPage.setPageStatus("100003");
        String url = cmsSite.getSiteDomain() + cmsSite.getSiteWebPath() + cmsPage.getPageWebPath() + cmsPage.getPageName();
        cmsPage.setPubUrl(url);
        this.update(pageId, cmsPage);

        return new ResponseResult(CommonCode.SUCCESS);
    }


    /**
     * 根据文件id从GridFS中查询文件内容
     * @return
     */
    public InputStream getFileById(String fileId) {
        //查询grid文件对象
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        //通过gridBucket根据grid文件对象的id打开下载流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //定义GridFsResource对象，获取流
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        try {
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 一键发布页面
     * @param cmsPage
     * @return
     */
    public CmsPostPageResult postPageQuick(CmsPage cmsPage) {
        //将页面信息存储到cms_page集合中
        CmsPageResult save = this.save(cmsPage);
        if (!save.isSuccess()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        CmsPage cmsPageSave = save.getCmsPage();
        String pageId = cmsPageSave.getPageId();
        //执行页面发布（静态化，保存GridFS，保存静态页面到本地）
        ResponseResult post = this.post(pageId);
        if (!post.isSuccess()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //拼接发布后的页面
        String siteId = cmsPageSave.getSiteId();
        CmsSite cmsSite = cmsSiteService.findCmsSiteById(siteId);
        if (cmsSite == null) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        CmsPage byId = this.getById(pageId);
        return new CmsPostPageResult(CommonCode.SUCCESS, byId.getPubUrl());

    }
}
