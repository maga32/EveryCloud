package com.project.everycloud.common.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
@Log4j2
public class LoggingInterceptor extends HandlerInterceptorAdapter {
    private final ObjectMapper objectMapper;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("==> RequestURI: {}", request.getRequestURI());

        if (request.getClass().getName().contains("SecurityContextHolderAwareRequestWrapper")) return;

        final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
        final ContentCachingResponseWrapper cachingResponse = (ContentCachingResponseWrapper) response;

        if (cachingRequest.getContentType() != null
                && cachingRequest.getContentType().contains("application/json")
                && cachingRequest.getContentAsByteArray().length != 0) {
            log.info("==> Req Params: {}", objectMapper.readTree(cachingRequest.getContentAsByteArray()));
        }

        if (cachingResponse.getContentType() != null
                && cachingResponse.getContentType().contains("application/json")
                && cachingResponse.getContentAsByteArray().length != 0) {
            log.info("<== Res Params: {}", objectMapper.readTree(cachingResponse.getContentAsByteArray()));
        }
    }
}
