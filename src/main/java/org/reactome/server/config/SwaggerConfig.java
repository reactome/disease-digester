package org.reactome.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {
    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("Disease Analysis Service")
//                .description("Provides an API for disease analysis by given a specific disease id")
//                .version("0.1")
//                .build();
        return new ApiInfo(
                "Disease Analysis Service",
                "Provides an API for disease analysis by given a specific disease id",
                "0.1",
                "AnalysisService/about/license-agreement",
                new Contact("Reactome", "https://reactome.org", "help@reactome.org"),
                "Creative Commons Attribution 3.0 Unsupported License",
                "http://creativecommons.org/licenses/by/3.0/legalcode",
                Collections.emptyList()
        );
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.reactome.server.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
