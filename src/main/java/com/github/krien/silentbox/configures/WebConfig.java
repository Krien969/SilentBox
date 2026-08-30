package com.github.krien.silentbox.configures;

import com.github.krien.silentbox.interceptors.FileProtectInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private FileProtectInterceptor fileProtectInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String basePath = System.getProperty("user.dir");

        registry.addResourceHandler("/file/public/**")
                .addResourceLocations("file:" + basePath + "/save/public/");

        registry.addResourceHandler("/file/protect/**")
                .addResourceLocations("file:" + basePath + "/save/protect/");

        registry.addResourceHandler("/file/protect/no-token/**")
                .addResourceLocations("file:" + basePath + "/save/protect/no-token/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedMethods("*")
                .allowedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(fileProtectInterceptor)
                .addPathPatterns(
                        "/file/protect/**",
                        "/file/protect/no-token/**"
                );
    }
}
