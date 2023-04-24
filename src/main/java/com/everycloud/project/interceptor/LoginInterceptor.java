package com.everycloud.project.interceptor;

import org.springframework.ui.Model;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getSession().getAttribute("user") == null) {
            String siteHtml = "";
            if(!request.getRequestURI().equals("")) {
                siteHtml += "?siteHtml=" + request.getRequestURI();
                if (!request.getQueryString().equals("")) {
                    siteHtml += URLEncoder.encode("?" + request.getQueryString(),"UTF-8");
                }
            }
            response.sendRedirect("/login" + siteHtml);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
