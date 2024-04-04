package com.erman.codechallenge;

import com.erman.codechallenge.services.HttpInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private HttpInterceptor httpInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("interceptor");
        registry.addInterceptor(httpInterceptor)
                .addPathPatterns("/v1/**") // Apply the interceptor to all URLs.
                .excludePathPatterns("/v1/auth/**"); // Exclude certain URLs from interception.
    }
    public static void main(String[] args) {
        SpringApplication.run(WebConfiguration.class, args);
    }
}
