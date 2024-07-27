package com.project.everycloud.common.config;

import com.project.everycloud.common.interceptor.LoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Value("${my.run-type}")
    private String myRunType;

    final LoggingInterceptor loggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if(myRunType.equals("dev")) {
            registry.addInterceptor(loggingInterceptor)
                    .order(0)
                    .addPathPatterns("/**")
                    .excludePathPatterns(
                            "/api/**/file/thumbnailMaker",
                            "/api/**/file/fileDownload",
                            "/api/**/file/getFavicon",
                            "/api/**/file/getMetaImage",
                            "/api/**/settings/getMeta",
                            "/error",
                            "/img/**",
                            "/assets/**"
                    );
        }
    }

}
