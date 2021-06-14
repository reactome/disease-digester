package org.reactome.server.config;

import com.mysql.cj.xdevapi.DatabaseObject;
import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "DisGeNET Analysis Service",
                "Provides an API for DisGeNET analysis by a specific disease id.\n" +
                        "<a href=\"http://www.disgenet.org/\" target=\"_blank\">DisGeNET</a> is a database of gene-disease associations. " +
                        "The Reactome pathway analysis for a given DisGeNET disease id is a two step process.<br/>" +
                        "For a DisGeNET disease id, /analyze/ return a JSON object, which in the \"url\" parameter contains the URL that is the direct link to the results visualisation.<br/>" +
                        "This URL will remain valid for at least seven days, but will eventually expire.",
                "0.1",
                "/license",
                new Contact("Reactome", "https://reactome.org", "help@reactome.org"),
                "Creative Commons Attribution 3.0 Unsupported License",
                "http://creativecommons.org/licenses/by/3.0/legalcode",
                Collections.emptyList()
        );
    }

    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.reactome.server.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
        /*remove the models info in swagger page*/
        Reflections reflections = new Reflections(DatabaseObject.class.getPackage().getName());
        for (Class<?> clazz : reflections.getSubTypesOf(DatabaseObject.class)) {
            docket.directModelSubstitute(clazz, Void.class);
        }
        return docket;
    }
}