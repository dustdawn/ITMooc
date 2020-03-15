package com.njit.framework.web;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author dustdawn
 * @date 2020/3/15 22:30
 */
public class BaseController {
    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected HttpSession session;

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {

        this.request = request;

        this.response = response;

        this.session = request.getSession();

    }
}
