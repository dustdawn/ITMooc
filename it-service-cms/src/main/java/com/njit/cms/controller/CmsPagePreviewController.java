package com.njit.cms.controller;

import com.njit.cms.service.CmsPageService;
import com.njit.framework.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletOutputStream;
import java.io.IOException;



/**
 * @author dustdawn
 * @date 2019/12/2 17:54
 */
@Controller
public class CmsPagePreviewController extends BaseController {

    @Autowired
    private CmsPageService cmsPageService;

    @GetMapping(value = "/cms/preview/{pageId}")
    public void preview(@PathVariable("pageId") String pageId) throws IOException {
        //执行静态化
        String pageHtml = cmsPageService.getPageHtml(pageId);
        //通过response对象将内容输出
        ServletOutputStream outputStream = response.getOutputStream();
        //由于Nginx先请求cms的课程预览功能得到html页面，再解析页面中的ssi标签，
        // 这里必须保证cms页面预览返回的页面的Content-Type为text/html;charset=utf-8
        // 必须以html输出，否则SSI无法解析
        response.setHeader("Content-type", "text/html;charset=utf-8");
        outputStream.write(pageHtml.getBytes("utf-8"));
    }
}
