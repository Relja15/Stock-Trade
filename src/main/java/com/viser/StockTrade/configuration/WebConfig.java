package com.viser.StockTrade.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Value("${myapp.custom.upload-dir}")
    private String UPLOAD_DIR;

    /**
     * Configures resource handlers for the application.
     *
     * This method adds resource handlers that enable access to files located in the upload directory
     * and static resources within the classpath.
     *
     * @param registry the {@link ResourceHandlerRegistry} object used to register resource handlers
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + UPLOAD_DIR);

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
